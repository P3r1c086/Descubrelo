package com.pedroaguilar.amigodeviaje.addModule

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.common.Constants
import com.pedroaguilar.amigodeviaje.common.entities.EventPost
import com.pedroaguilar.amigodeviaje.common.entities.Sujerencia
import com.pedroaguilar.amigodeviaje.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private var binding: FragmentAddBinding? = null

    private var sujerencia: Sujerencia? = null

    //variables globales para cargar imagen en la imageView o subirlo a cloud Storage
    private var photoSelectedUri: Uri? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            photoSelectedUri = it.data?.data

            // binding?.imgProductPreview?.setImageURI(photoSelectedUri)

            //Glide tb puede cargar una imagen que venga localmente
            binding?.let {
                //cargar imagen
                Glide.with(this)
                    .load(photoSelectedUri)
                    //este es para que almacene la imagen descargada, para que no tenga que estar
                    // consultando cada vez que inicie la app. Tiene la desventaja que hasta que no cambie
                    // la url, la imagen va a ser la misma sin importar que el servidor si cambie
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(it.imgSuggestionPreview)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configButtons()
        listener()
    }



    private fun listener() {

        binding?.btnAceptar?.setOnClickListener {
            //creamos un nuevo producto. El id debe ser generado automaticamente. Aqui lo omitimos
            //y empezamos por la propiedad name
            //como binding puede ser null
            binding?.let {
                //subir imagen al storage. Recibe un callBack
                uploadImage(sujerencia?.id) { eventPost ->
                    //si la imagen fue subida correctamente
                    if (eventPost.isSuccess){
                        if (sujerencia == null){//si el producto es null, lo creamos
                            val sujerencia = Sujerencia(name = it.etNombreSuj.text.toString().trim(),
                                description = it.etDescriptionSuj.text.toString().trim(),
                                imgUrl = eventPost.photoUrl)

                            save(sujerencia, eventPost.documentId!!)
                        }else{//si no es null, retomamos nuestro producto y le damos los nuevos valores,
                            //es decir, es una actualizacion
                            sujerencia?.apply {
                                name = it.etNombreSuj.text.toString().trim()
                                description = it.etDescriptionSuj.text.toString().trim()
                                imgUrl = eventPost.photoUrl
                                //una vez tenemos el producto con las nuevas propiedades, llamamos a un
                                // metodo que lo actualice
//                                update(this)//debido a apply, en vez poner el producto, ponemos el
                                //contexto
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configButtons(){
        binding?.let {
            it.ibSuggestion.setOnClickListener {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    /**
     * metodo para subir imagenes al storage
     */
    private fun uploadImage(sujerenciaId: String?, callback: (EventPost)->Unit){ //que retorna Unit sig que no retorna nada
        //creamos una nueva instancia de EventPost, la cual va a contener el documento
        val eventPost = EventPost()
        //El sigo de Elvis, hace que en caso de que sea null, agarre el id del nuevo documento, sino
        // que se quede con el id del producto actual.
        //extraemos el id del document. Estamos reservando un lugar para que la imagen que subamos
        // tenga como nombre este id. Posteriormente, una vez que termine el proceso de subir vamos a
        // regresar ese documento para que la imagen que vayamos a subir sea asignada con el nombre
        // de este id y posteriormente, despues de que se suba nuestra imagen, ahora si, vamos a
        // agarra el mismo document id para insertar un nuevo registro
        eventPost.documentId = sujerenciaId ?: FirebaseFirestore.getInstance().collection(Constants.COLL_SUGGEST)
            .document().id
        //hacemos una instancia a la raiz del servidor
        val storageRef = FirebaseStorage.getInstance().reference
            //ponemos como hijo una carpeta donde almacenar las imagenes
            .child(Constants.PATH_SUGGEST_IMAGES)
        //si photoSelectedUri es != de null y binding tb
        photoSelectedUri?.let { uri ->
            binding?.let { binding ->
                //hacemos visible el progressbar
                binding.progressBar.visibility = View.VISIBLE
                //creamos una nueva referencia que apunta al id de la foto
                val photoRef = storageRef.child(eventPost.documentId!!)
                //comenzamos a subir la imagen. uri es photoSelectedUri
                photoRef.putFile(uri)
                    //para la barra de progreso al subir la foto
                    .addOnProgressListener {
                        //con esto obtenemos los bytes tranferidos respecto al total
                        val progress = (100 * it.bytesTransferred / it.totalByteCount).toInt()
                        it.run {
                            binding.progressBar.progress = progress
                            binding.tvProgress.text = String.format("%s%%", progress)
                        }
                    }
                    .addOnSuccessListener {
                        //extraemos la url para descargar
                        it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                            Log.i("URL", downloadUrl.toString())
                            //la imagen ya ha sido subida al storage con putFile, ahora vamos a insertarla en Firestore
                            eventPost.isSuccess = true
                            eventPost.photoUrl = downloadUrl.toString()
                            callback(eventPost)
                        }
                    }
                    .addOnFailureListener{
                        Toast.makeText(activity, "Error al subir imagen.", Toast.LENGTH_SHORT).show()
                        eventPost.isSuccess = false
                        callback(eventPost)
                    }
            }
        }
    }

    private fun save(sujerencia: Sujerencia, documentId: String){
        //creamos una instancia de la base de datos de Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.COLL_SUGGEST)
            //le seteamos el id de forma manual
            .document(documentId)
            .set(sujerencia)
//            .add(product)
            .addOnSuccessListener {
                Toast.makeText(activity, "Sujerencia añadida.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Error al insertar.", Toast.LENGTH_SHORT).show()

            }
            .addOnCompleteListener {
                //una vez haya terminado el proceso de insercion, se habilitara
                // el dialog, independientemente de si el proceso fue exitoso o no
//                enableUI(true)
                //Unicamente sera mostrado el progressbar cuando haya una subida en proceso, es decir,
                //cuando se termine se ocultara
                binding?.progressBar?.visibility = View.INVISIBLE
                //cerrar el dialog fragment
//                dismiss()
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        //para poder desvincular binding
        binding = null
    }
}