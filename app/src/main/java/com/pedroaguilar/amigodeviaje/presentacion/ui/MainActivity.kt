package com.pedroaguilar.amigodeviaje.presentacion.ui


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.pedroaguilar.amigodeviaje.R
import com.pedroaguilar.amigodeviaje.common.Error.Connectivity
import com.pedroaguilar.amigodeviaje.common.Error.Server
import com.pedroaguilar.amigodeviaje.common.launchAndCollect
import com.pedroaguilar.amigodeviaje.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
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

        // Estructura que se usa para enganchar la vista a las actualizaciones del estado en el viewmodel
        //En un fragmento sería: viewLifecycleOwner.launchAndCollect(viewModel.state) {}

        lifecycle.launchAndCollect(viewModel.state) {
            binding.loading = it.loading
            binding.usuario = it.usuario
            binding.error = it.error?.let(::errorToString)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.bottomNavigationView.background = null

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
            if (auth.currentUser != null){ //si existe el usuario
                //coloco el nombre del usuario en la barra de la app
                FirebaseAuth.getInstance().uid?.let { id ->
                    FirebaseAuth.getInstance().currentUser?.displayName?.let { name ->
                        viewModel.registrarUsuarioEnFirebaseDatabase(id, name)
                    }
                }
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
                R.id.home_dest, R.id.profile_dest, R.id.settings_dest, R.id.favorite_dest -> showBottomNav()
                R.id.details_dest -> hideBottomNav(hideToolbar = true)
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.fab.visibility = View.VISIBLE
        binding.toolbar.navigationIcon = null
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun hideBottomNav(hideToolbar: Boolean = false) {
        binding.bottomAppBar.visibility = View.GONE
        binding.fab.visibility = View.GONE
        if (hideToolbar){
            binding.toolbar.visibility = View.GONE
        } else {
            binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        }

    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun errorToString(error: com.pedroaguilar.amigodeviaje.common.Error) = when (error) {
        Connectivity -> applicationContext.getString(R.string.connectivity_error)
        is Server -> applicationContext.getString(R.string.server_error) + error.code
        else -> applicationContext.getString(R.string.unknown_error)
    }
}