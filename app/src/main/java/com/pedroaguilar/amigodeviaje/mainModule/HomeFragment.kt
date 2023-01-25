package com.pedroaguilar.amigodeviaje.mainModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.databinding.FragmentHomeBinding
import com.pedroaguilar.amigodeviaje.common.utils.HomeAux
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val list = mutableListOf<CarouselItem>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCarruselImagesList()
    }

//    override fun goToTop() {
////        binding.recyclerView.smoothScrollToPosition(0)
//    }

    private fun loadCarruselImagesList(){
        val carousel: ImageCarousel = binding.carousel
        list.add(CarouselItem(imageDrawable = R.drawable.alhambra, caption = "Alhambra") )
        list.add(CarouselItem(imageDrawable = R.drawable.playa_granada, caption = "Playa Granada"))
        list.add(CarouselItem(imageDrawable = R.drawable.sierra_nevada, caption = "Sierra Nevada"))
        //carousel.autoPlay = true
        carousel.addData(list)
    }
}