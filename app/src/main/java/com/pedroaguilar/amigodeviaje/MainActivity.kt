package com.pedroaguilar.amigodeviaje


import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.pedroaguilar.amigodeviaje.addModule.AddFragment
import com.pedroaguilar.amigodeviaje.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var activityFragment: Fragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: AuthStateListener

    private lateinit var navController: NavController

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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        configAuth()
        setupBottomNav()
        listener()
    }

    private fun listener() {
        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_addFragment)
        }
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
        // Pasar cada ID de menú como un conjunto de Ids porque cada menú debe ser considerado como destinos de primer nivel.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        val mAppBarConfiguration: AppBarConfiguration = Builder(
            navController.graph)
            .build()
        setupWithNavController(binding.toolbar, navController, mAppBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_dest, R.id.profile_dest, R.id.settings_dest, R.id. favorite_dest-> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.fab.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomAppBar.visibility = View.GONE
        binding.fab.visibility = View.GONE
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