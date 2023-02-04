package com.pedroaguilar.amigodeviaje.presentacion.ui.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.common.Error.Connectivity
import com.pedroaguilar.amigodeviaje.common.Error.Server
import com.pedroaguilar.amigodeviaje.databinding.FragmentAddBinding
import com.pedroaguilar.amigodeviaje.modelo.launchAndCollect
import com.pedroaguilar.amigodeviaje.modelo.loadUrl
import com.pedroaguilar.amigodeviaje.presentacion.ui.add.viewModel.AddFragmentViewModel


class AddFragment : Fragment() {

    private val viewModel: AddFragmentViewModel by viewModels()

    private lateinit var binding: FragmentAddBinding

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            viewModel.setImageUrl(it.data?.data.toString())
            binding.imgSuggestionPreview.loadUrl(it.data?.data.toString())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            binding.loading = it.loading
            binding.progressLoading = it.progressLoading
            binding.progressText = if (!it.sugerenciaSubidaCorrectamente) {
                String.format("%s%%", it.progressLoading)
            } else {
                "Sugerencia subida con éxito!\n Cerrando la pantalla"
            }
            binding.error = it.error?.let(::errorToString)
            binding.habilitarAceptar = !it.category.isNullOrBlank()
                        && seleccionCorrectaSpinner()
                    && !it.nombre.isNullOrBlank()
                    && !it.descripcion.isNullOrBlank()
                    && !it.photoSelectedUri.isNullOrBlank()
                    && !it.loading
                    && !it.sugerenciaSubidaCorrectamente
            if (it.sugerenciaSubidaCorrectamente){
                cerrarPantalla()
            }
        }
        configButtons()
        listener()
        spinnerListener()
    }

    private fun cerrarPantalla(){
        Handler(Looper.getMainLooper()).postDelayed({
           activity?.onBackPressedDispatcher?.onBackPressed()
        }, 2000)
    }

    private fun seleccionCorrectaSpinner(): Boolean{
        return binding.spTypeCategory.selectedItemPosition != 0
    }

    private fun spinnerListener(){
        binding.rgSuggestion.setOnCheckedChangeListener { _, _ ->
            when {
                binding.rbEat.isChecked -> {
                    viewModel.setCategory("comer")
                    cargarSpinner(R.array.sujerencias_comer)
                }
                binding.rbSleep.isChecked -> {
                    viewModel.setCategory("dormir")
                    cargarSpinner(R.array.sujerencias_dormir)
                }
                binding.rbParty.isChecked -> {
                    viewModel.setCategory("fiesta")
                    cargarSpinner(R.array.sujerencias_fiesta)
                }
                binding.rbTourism.isChecked -> {
                    viewModel.setCategory("turismo")
                    cargarSpinner(R.array.sujerencias_turismo)
                }
                binding.rbAdventure.isChecked -> {
                    viewModel.setCategory("aventura")
                    cargarSpinner(R.array.sujerencias_aventura)
                }
            }
        }
    }

    private fun cargarSpinner(arrayRes: Int){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(), arrayRes, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spTypeCategory.adapter = adapter
        }
    }

    private fun listener() {
        binding.etNombreSuj.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { viewModel.setName(s.toString())}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etDescriptionSuj.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { viewModel.setDescription(s.toString())}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.spTypeCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setTypeCategory(parent?.getItemAtPosition(position).toString())
            }
        }
        binding.btnAceptar.setOnClickListener {
            viewModel.onClickAccept()
        }
    }

    private fun configButtons(){
        binding.ibSuggestion.setOnClickListener { openGallery() }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }


    private fun errorToString(error: com.pedroaguilar.amigodeviaje.common.Error) = when (error) {
        Connectivity -> requireContext().getString(R.string.connectivity_error)
        is Server -> requireContext().getString(R.string.server_error) + error.code
        else -> requireContext().getString(R.string.unknown_error)
    }
}