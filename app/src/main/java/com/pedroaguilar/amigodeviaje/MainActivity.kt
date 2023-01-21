package com.pedroaguilar.amigodeviaje

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.pedroaguilar.amigodeviaje.addModule.AddFragment
import com.pedroaguilar.amigodeviaje.databinding.ActivityMainBinding
import com.pedroaguilar.amigodeviaje.mainModule.HomeFragment
import com.pedroaguilar.amigodeviaje.profileModule.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var activityFragment: Fragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: AuthStateListener

    private val resultLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //Dentro proceso la respuesta
        val response = IdpResponse.fromResultIntent(it.data)

        if (it.resultCode == RESULT_OK){
            //compruebo que exista un usuario autenticado
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null){
                Toast.makeText(this, getString(R.string.welcome_message), Toast.LENGTH_SHORT).show()
            }
        }else{
            //para salir de la app al darle hacia atras en login
            if (response == null){
                Toast.makeText(this, getString(R.string.bye_message), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                response.error?.let {
                    if (it.errorCode == ErrorCodes.NO_NETWORK){
                        Toast.makeText(this, getString(R.string.no_red), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Código de error: ${it.errorCode}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNav()
        configAuth()
    }

    private fun configAuth() {
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = AuthStateListener { auth ->
            if (auth.currentUser != null){//si existe el usuario
                //coloco el nombre del usuario en la barra de la app
                supportActionBar?.title = auth.currentUser?.displayName
                //oculto el spinner
                binding.llProgress.visibility = View.GONE
            }else{//si no existe el usuario
                //Autenticar desde Firebase
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())

                //Lanzar actividad para esperar un resultado
               resultLauncher.launch(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .build())
            }
        }
    }


    /**
     * Agregar los fragmentos al contenedor para controlarlos desde los botones de navegacion
     */
    private fun setupBottomNav() {
        //Inicializar el FragmentManager
        fragmentManager = supportFragmentManager

        //Instanciar todos los fragmentos disponibles en este proyecto
        val homeFragment = HomeFragment()
        val addFragment = AddFragment()
        val profileFragment = ProfileFragment()

        activityFragment = homeFragment

        /**
         * Mediante el fragmentManager controlaremos cual se muestra en cada momento.
         * Empezamos por el ultimo y terminamos por el primero.
         * "hostFragment" es el contenedor, "ProfileFragment" el fragmento y "ProfileFragment::class.java.name" la etiqueta
         */
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, profileFragment, ProfileFragment::class.java.name)
            .hide(profileFragment).commit()//Lo añadimos y a la vez lo ocultamos
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, addFragment, AddFragment::class.java.name)
            .hide(addFragment).commit()//Lo añadimos y a la vez lo ocultamos
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, homeFragment, HomeFragment::class.java.name).commit()//Lo añadimos y a la vez lo ocultamos

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.action_home -> {
                    //Ocultamos el fragmento activo xq no sabemos cual es y mosrtamos homeFragment
                    fragmentManager.beginTransaction().hide(activityFragment).show(homeFragment).commit()
                    activityFragment = homeFragment //Ponemos el homeFragment como el fragment activo
                    true//devolvemos un true
                }
                R.id.action_add -> {
                    //Ocultamos el fragmento activo xq no sabemos cual es y mosrtamos homeFragment
                    fragmentManager.beginTransaction().hide(activityFragment).show(addFragment).commit()
                    activityFragment = addFragment //Ponemos el homeFragment como el fragment activo
                    true//devolvemos un true
                }
                R.id.action_profile -> {
                    //Ocultamos el fragmento activo xq no sabemos cual es y mosrtamos homeFragment
                    fragmentManager.beginTransaction().hide(activityFragment).show(profileFragment).commit()
                    activityFragment = profileFragment //Ponemos el homeFragment como el fragment activo
                    true//devolvemos un true
                }
                else -> false
            }
        }
        //volver a la parte de arriba de la app al pulsar
//        binding.bottomNav.setOnItemReselectedListener{
//            when(it.itemId){
//                R.id.action_home -> (homeFragment as HomeAux).goToTop()
//            }
//        }

    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

}