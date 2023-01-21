package com.pedroaguilar.amigodeviaje.addModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedroaguilar.amigodeviaje.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAdd.text = "AGREGAR"
    }

}