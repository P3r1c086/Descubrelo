package com.pedroaguilar.amigodeviaje.presentacion.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.pedroaguilar.amigodeviaje.FiestaFragment
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.databinding.FragmentHomeBinding
import com.pedroaguilar.amigodeviaje.modelo.launchAndCollect
import com.pedroaguilar.amigodeviaje.presentacion.ui.MainActivity
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter.SugerenciaAdapter
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.viewModel.HomeFragmentViewModel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class HomeFragment : Fragment() {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private val adapter: SugerenciaAdapter = SugerenciaAdapter(ArrayList())

    private val list : List<CarouselItem> = listOf(
        CarouselItem(imageDrawable = R.drawable.alhambra, caption = "Alhambra"),
        CarouselItem(imageDrawable = R.drawable.playa_granada, caption = "Playa Granada"),
        CarouselItem(imageDrawable = R.drawable.sierra_nevada, caption = "Sierra Nevada")
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            adapter.dataSet = it.sugerencias
            binding.carousel3d.adapter = adapter
            binding.carousel3d.apply {
                set3DItem(true)
                setAlpha(true)
                //setInfinite(true)
            }
        }
//        loadCarruselImagesList()
        loadCarrusel3dImagesList()
    }

//    private fun loadCarruselImagesList(){
//        with (binding.carouselWeekend){
//            autoPlay = true
//            addData(list)
//            carouselListener = object : CarouselListener {
//                override fun onClick(position: Int, carouselItem: CarouselItem) {
//                    //Todo: enviar argumentos con informacion del item pulsado -> Correcto!! Eso te falta
//                    ((activity as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
//                        .navController.navigate(R.id.action_homeFragment_to_detailsFragment)
//                }
//            }
//        }
//    }

    private fun loadCarrusel3dImagesList(){
        //todo: He intentado hacer el clickListener desde aqui y desde el adapter, pero no me sale
    }

}