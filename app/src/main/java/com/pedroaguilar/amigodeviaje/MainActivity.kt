package com.pedroaguilar.amigodeviaje

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.pedroaguilar.amigodeviaje.databinding.ActivityMainBinding
import com.pedroaguilar.amigodeviaje.utils.HomeAux

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var activityFragment: Fragment
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNav()
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
        binding.bottomNav.setOnItemReselectedListener{
            when(it.itemId){
                R.id.action_home -> (homeFragment as HomeAux).goToTop()
            }
        }

    }
}