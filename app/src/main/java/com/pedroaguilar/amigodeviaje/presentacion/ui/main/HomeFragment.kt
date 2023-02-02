package com.pedroaguilar.amigodeviaje.presentacion.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.pedroaguilar.amigodeviaje.presentacion.ui.MainActivity
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.databinding.FragmentHomeBinding
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.adapter.SugerenciaAdapter
import com.pedroaguilar.amigodeviaje.presentacion.ui.main.viewModel.HomeFragmentViewModel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: SugerenciaAdapter
    private lateinit var viewModel: HomeFragmentViewModel
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

        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            adapter = SugerenciaAdapter(it)
            binding.carousel3d.adapter = adapter
            binding.carousel3d.apply {
                set3DItem(true)
                setAlpha(true)
                setInfinite(true)

            }
        })
        loadCarruselImagesList()
    }

    private fun loadCarruselImagesList(){
        with (binding.carouselWeekend){
            autoPlay = true
            addData(list)
            carouselListener = object : CarouselListener {
                override fun onClick(position: Int, carouselItem: CarouselItem) {
                    //Todo: enviar argumentos con informacion del item pulsado
                    ((activity as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                        .navController.navigate(R.id.action_homeFragment_to_detailsFragment)
                }
            }
        }
    }
}