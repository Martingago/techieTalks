package com.martingago.blog_retrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.martingago.blog_retrofit.databinding.ActivityMainBinding
import com.martingago.blog_retrofit.interfaces.UserLoginStatusChangeListener
import com.martingago.blog_retrofit.network.SharedPreferencesManager


class MainActivity : AppCompatActivity(), UserLoginStatusChangeListener {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navigationViewMenu: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesManager = SharedPreferencesManager(this) //Se consigue instancia de los sharedPreferences

        drawerLayout = findViewById(R.id.drawer_layout) //Sección global del menu principal
        navigationView = findViewById(R.id.nav_view)
        navigationViewMenu = findViewById(R.id.nav_view_menu) //Menu lateral de la app
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        applyUserPreferences() //Preferencias de idioma y theme por defecto

        // Establece el navController en el fragmentSectionArea que controla las vistas que se cargan de cada sección
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentSectionArea) as NavHostFragment
        navController = navHostFragment.navController
        handleNavigationButtons()
        handleLinks()
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun handleNavigationButtons(){
        // Si el usuario está logueado, mostramos las opciones de cuenta y ocultamos las de login/register
        if (sharedPreferencesManager.isLoggedIn()) {
            navigationViewMenu.menu.findItem(R.id.nav_account).isVisible = true
            navigationViewMenu.menu.findItem(R.id.nav_logout).isVisible = true
            navigationViewMenu.menu.findItem(R.id.nav_login).isVisible = false
            navigationViewMenu.menu.findItem(R.id.nav_register).isVisible = false
            navigationViewMenu.menu.findItem(R.id.section_editor).isVisible = false
            //Section roles de EDITOR/ADMIN
            val roles = sharedPreferencesManager.getRoles()
            if (roles.any { it == "EDITOR" || it == "ADMIN" }){
                navigationViewMenu.menu.findItem(R.id.section_editor).isVisible = true
            }
            if(roles.any{ it == "ADMIN"}){
                navigationViewMenu.menu.findItem(R.id.section_admin).isVisible = true
            }

        } else {
            // Si no está logueado, mostramos las opciones de login/register y ocultamos las de cuenta
            navigationViewMenu.menu.findItem(R.id.nav_account).isVisible = false
            navigationViewMenu.menu.findItem(R.id.nav_logout).isVisible = false
            navigationViewMenu.menu.findItem(R.id.nav_login).isVisible = true
            navigationViewMenu.menu.findItem(R.id.nav_register).isVisible = true
            navigationViewMenu.menu.findItem(R.id.section_editor).isVisible = false
            navigationViewMenu.menu.findItem(R.id.section_admin).isVisible = false
        }
    }

    //Maneja las navegaciones de los enlaces existentes
    private fun handleLinks() {
        navigationViewMenu.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> navController.navigate(R.id.sectionIndexFragment)
                R.id.nav_settings ->  navController.navigate(R.id.sectionSettingsFragment)
                R.id.nav_account -> navController.navigate(R.id.sectionAccountFragment)
                R.id.nav_login -> navController.navigate(R.id.sectionLoginFragment)
                R.id.nav_register -> navController.navigate(R.id.sectionRegisterFragment)
                R.id.nav_create_post -> navController.navigate(R.id.sectionAddPublicationFragment)
                R.id.nav_table_post -> navController.navigate(R.id.sectionManagePublicationsFragment)
                R.id.nav_create_tag -> navController.navigate(R.id.sectionAddTagFragment)
                R.id.nav_table_tag -> navController.navigate(R.id.sectionManageTagsFragment)
                R.id.nav_table_user -> navController.navigate(R.id.sectionManageUsersFragment)
                R.id.nav_logout -> logoutUser()
                R.id.nav_legal -> navController.navigate(R.id.sectionLegalFragment)

                else -> false
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun restartActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    //Actualiza los botones en base al estado de login del usuario
    override fun updateMenuBasedOnLoginStatus() {
        handleNavigationButtons()
    }

    private fun logoutUser(){
        sharedPreferencesManager.clearCredentials() //Limpia las credenciales del usuario
        // Notificar a la actividad para que actualice el menú
        updateMenuBasedOnLoginStatus()
        Toast.makeText(this, getString(R.string.message_logout_success), Toast.LENGTH_SHORT).show()
        navController.navigate(R.id.action_global_sectionIndexFragment)
    }

    /**
     * Función que establece los valores por defecto de la aplicación en base al dispositivo:
     * - IDIOMA
     * - THEME
     */
    private fun applyUserPreferences() {
        val language = sharedPreferencesManager.getLanguagePreference()
        val theme = sharedPreferencesManager.getThemePreference()

        // Aplicar idioma
        if (language != "default") {
            val locale = when (language) {
                "English" -> "en"
                "Español" -> "es"
                else -> null
            }
            locale?.let {
                val config = resources.configuration
                config.setLocale(java.util.Locale(it))
                createConfigurationContext(config)
            }
        }
        when (theme) {
            "Light" , "Modo claro" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Dark" , "Modo oscuro" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }


}






