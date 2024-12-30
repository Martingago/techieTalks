package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.controller.UserController
import com.martingago.blog_retrofit.interfaces.UserLoginStatusChangeListener
import com.martingago.blog_retrofit.model.user.UserRegisterRequest
import com.martingago.blog_retrofit.model.user.UserResponseItem
import com.martingago.blog_retrofit.network.ApiClient
import com.martingago.blog_retrofit.network.SharedPreferencesManager
import com.martingago.blog_retrofit.service.UserService
import com.martingago.blog_retrofit.utils.NavUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterFragment : Fragment() {

    private lateinit var userService: UserService
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var userController: UserController
    //loader
    private lateinit var  registerForm : View
    private lateinit var loadingLayout : View
    private lateinit var loadingMsg : TextView

    //Elementos del formulario
    private lateinit var usernameEditText :EditText
    private lateinit var nameEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var confirmPasswordEditText : EditText
    private lateinit var acceptTermsCheckbox : CheckBox
    private lateinit var registerButton : Button

    private lateinit var registerErrorMessageEditText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        userController = UserController(requireContext())
        userService  =  ApiClient.getInstanceWithoutAuth().create(UserService::class.java)
        registerForm = view.findViewById(R.id.registerForm)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        // Inflate the layout for this fragment
        usernameEditText = view.findViewById(R.id.usernameEditText)
        nameEditText = view.findViewById(R.id.nameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        confirmPasswordEditText = view.findViewById(R.id.passwordConfirmEditText)
        acceptTermsCheckbox = view.findViewById(R.id.termsUseCheck)
        registerButton = view.findViewById(R.id.registerButton)
        registerErrorMessageEditText = view.findViewById(R.id.registerErrorMessage)
        //Loading
        loadingLayout = view.findViewById(R.id.registerLoader)
        loadingMsg = view.findViewById(R.id.registerMsg)


        //Listener en el botón de register:
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()

            if(validateForm()){
                val userRegisterRequest = UserRegisterRequest(username,password,name)
                registerNewUser(userRegisterRequest)
            }
        }
        return view
    }

    private fun registerNewUser(userRegisterRequest: UserRegisterRequest){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try{
                val response = withContext(Dispatchers.IO){
                    userController.registerUser(
                        userRegisterRequest,
                        updateStatus = {currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if(response.success == true){
                    val userCredentials = response.objectResponse
                    if(userCredentials != null){
                        handleSuccessfullyRegister(userCredentials, userRegisterRequest.password)
                        (activity as? UserLoginStatusChangeListener)?.updateMenuBasedOnLoginStatus()
                        NavUtils.navigateBackAvoidingLoginRegister(this@RegisterFragment) //Mueve al usuario al backstack
                    }
                }else{
                    hiddeLoader(response.message)
                }

            }catch (e : Exception){
                showToast(e.toString())
            }
        }
    }

    private fun handleSuccessfullyRegister(userResponseItem: UserResponseItem, password : String){
        sharedPreferencesManager.saveUserCredentials(
            id = userResponseItem.id,
            username = userResponseItem.username,
            password = password,
            name = userResponseItem.name,
            roles = userResponseItem.userRoles
        )
        showToast(getString(R.string.toast_successfully_register))
        sharedPreferencesManager.saveLoginState(true) //Marca el estado de inicio de sesión como TRUE
    }

    private fun showLoader(currentAttempt : Int, maxAttempt : Int){
        loadingMsg.text = getString(R.string.register_user_loading_msg, currentAttempt, maxAttempt)
        loadingLayout.visibility = View.VISIBLE
        registerForm.visibility = View.GONE
    }

    private fun hiddeLoader(message : String){
        loadingLayout.visibility = View.GONE
        registerForm.visibility = View.VISIBLE
        registerErrorMessageEditText.text = message ?: getString(R.string.unknown_error)
        registerErrorMessageEditText.visibility = View.VISIBLE

    }

    private fun showToast(message : String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    //Valida que todos los campos del usuario estén completos
    private fun validateForm(): Boolean {
        val username = usernameEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()
        val isTermsAccepted = acceptTermsCheckbox.isChecked

        // Reinicia el mensaje de error
        registerErrorMessageEditText.visibility = View.GONE

        when {
            username.isEmpty() -> {
                registerErrorMessageEditText.text = getString(R.string.error_empty_username)
                registerErrorMessageEditText.visibility = View.VISIBLE
                return false
            }
            name.isEmpty() -> {
                registerErrorMessageEditText.text = getString(R.string.error_empty_name)
                registerErrorMessageEditText.visibility = View.VISIBLE
                return false
            }
            password.isEmpty() -> {
                registerErrorMessageEditText.text = getString(R.string.error_empty_password)
                registerErrorMessageEditText.visibility = View.VISIBLE
                return false
            }
            confirmPassword.isEmpty() -> {
                registerErrorMessageEditText.text = getString(R.string.error_empty_confirm_password)
                registerErrorMessageEditText.visibility = View.VISIBLE
                return false
            }
            password != confirmPassword -> {
                registerErrorMessageEditText.text = getString(R.string.error_password_mismatch)
                registerErrorMessageEditText.visibility = View.VISIBLE
                return false
            }
            !isTermsAccepted -> {
                registerErrorMessageEditText.text = getString(R.string.error_terms_unchecked)
                registerErrorMessageEditText.visibility = View.VISIBLE
                return false
            }
            else -> {
                return true // Todos los campos son válidos
            }
        }
    }

}