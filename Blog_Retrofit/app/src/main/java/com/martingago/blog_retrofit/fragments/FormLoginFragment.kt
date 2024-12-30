package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.controller.UserController
import com.martingago.blog_retrofit.interfaces.UserLoginStatusChangeListener
import com.martingago.blog_retrofit.model.user.UserRequest
import com.martingago.blog_retrofit.model.user.UserResponseItem
import com.martingago.blog_retrofit.network.SharedPreferencesManager
import com.martingago.blog_retrofit.utils.NavUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FormLoginFragment : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var userController : UserController
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    // Referencias al formulario
    private lateinit var loginForm: View
    private lateinit var loadingLayout: View
    //Loading references
    private lateinit var loginErrorMsg : TextView
    private lateinit var loadingMsg : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form_login, container, false)

        usernameEditText = view.findViewById(R.id.usernameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton) //Boton de login
        loginForm = view.findViewById(R.id.loginForm) //Formulario
        loginErrorMsg = view.findViewById(R.id.loginErrorMessage) //mensaje error de login
        loadingLayout = view.findViewById(R.id.loginLoader) //Loading mientras validan datos
        loadingMsg = view.findViewById(R.id.loadingMsg) // Mensaje del loading

        // Inicializar el controller y las sharedPreferences
        userController = UserController(requireContext())
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        loginButton.isEnabled = false //Por defecto el boton está deshabilitado

        // Configurar el botón de inicio de sesión
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            performLogin(username, password)
        }

        // Agregar TextWatchers para habilitar/deshabilitar el botón
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateLoginButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        return view
    }

    private fun updateLoginButtonState() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        loginButton.isEnabled = username.isNotEmpty() && password.isNotEmpty()
    }

    //
    private fun performLogin(username: String, password: String) {
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.Main){
            try{
                val userRequest = UserRequest(username, password)
                //Realiza la petición al API
                val response = withContext(Dispatchers.IO){
                    userController.loginUser(
                        userRequest,
                        updateStatus = {currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if(response.success == true){
                    val userCredentials = response.objectResponse
                    if(userCredentials != null){
                        handleSuccessfullyLogin(userCredentials, password)
                    }
                }else{
                    hiddeLoader(response.message)
                }
            }catch (e : Exception){
                showError(e.toString()) // Manejar cualquier error no relacionado con la respuesta del servidor
            }
        }
    }

    /**
     * Función que maneja cuando el usuario ha realizado el login de forma correcta
     * @param userCredentials datos que recibe la aplicación desde el servidor
     * @param password contraseña local que ha introducido que se almacena para enviar en los headers de las peticiones
     */
    private fun handleSuccessfullyLogin(userCredentials: UserResponseItem?, password: String){
        userCredentials?.let {
            sharedPreferencesManager.saveUserCredentials(
                id = it.id,
                username = it.username,
                password = password,
                name = it.name,
                roles = it.userRoles
            )
            sharedPreferencesManager.saveLoginState(true)
        }
        saveCredentialsAndRedirect()
    }
    private  fun showLoader(currentAttempt : Int, maxAttempt : Int){
        loadingLayout.visibility = View.VISIBLE
        loginForm.visibility = View.GONE
        loginErrorMsg.visibility = View.GONE
        loadingMsg.text = getString(R.string.loading_credentials_attempt, currentAttempt, maxAttempt)
    }

    private fun saveCredentialsAndRedirect(){
        // Notificar a la actividad para que actualice el menú
        loadingLayout.visibility = View.GONE
        (activity as? UserLoginStatusChangeListener)?.updateMenuBasedOnLoginStatus() //Actualiza el menu de estado

        Toast.makeText(requireContext(), getString(R.string.login_successfully), Toast.LENGTH_SHORT).show()
        NavUtils.navigateBackAvoidingLoginRegister(this@FormLoginFragment) //Mover al usuario a la ultima vista
    }

    private fun showError(message : String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    //Oculta el loader, actualiza y muestra el mensaje de login y limpia credenciales
    private fun hiddeLoader(errorMessage : String){
        loadingLayout.visibility = View.GONE
        loginForm.visibility = View.VISIBLE
        loginErrorMsg.visibility = View.VISIBLE
        loginErrorMsg.text = errorMessage
        passwordEditText.text.clear()
    }



}