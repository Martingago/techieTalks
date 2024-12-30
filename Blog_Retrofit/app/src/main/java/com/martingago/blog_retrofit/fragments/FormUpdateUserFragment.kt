package com.martingago.blog_retrofit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.model.APIResponse
import com.martingago.blog_retrofit.model.user.UserResponseItem
import com.martingago.blog_retrofit.model.user.UserUpdate
import com.martingago.blog_retrofit.network.ApiClient
import com.martingago.blog_retrofit.network.SharedPreferencesManager
import com.martingago.blog_retrofit.service.UserService
import kotlinx.coroutines.launch
import retrofit2.Response

class FormUpdateUserFragment : Fragment() {

    private lateinit var userService: UserService
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    //loader
    private lateinit var updateUserForm : View
    private lateinit var loadingLayout : View

    //Elementos del formulario
    private lateinit var nameEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var confirmPasswordEditText : EditText
    private lateinit var updateUserButton : Button
    private lateinit var updateErrorMessageEditText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_form_update_user, container, false)

        userService  =  ApiClient.getInstanceWithAuth(requireContext()).create(UserService::class.java)
        updateUserForm = view.findViewById(R.id.updateUserForm)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        // Inflate the layout for this fragment
        nameEditText = view.findViewById(R.id.nameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        confirmPasswordEditText = view.findViewById(R.id.passwordConfirmEditText)

        updateUserButton = view.findViewById(R.id.updateUserButton)
        updateErrorMessageEditText = view.findViewById(R.id.updateErrorMessage)

        loadingLayout = view.findViewById(R.id.registerLoader)
        //Carga datos default del form
        fillFormWithUserCurrentData()

        //Listener en el botón de register:
        updateUserButton.setOnClickListener {
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()
            val userUpdate = UserUpdate(name, password)
            if(validateForm()){
                updateUserData(userUpdate)
            }
        }

        return view
    }

    // Función que valida que los datos del formulario para actualizar los datos del usuario son correctos.
    private fun validateForm() : Boolean{
        val name = nameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        // Reinicia el mensaje de error
        updateErrorMessageEditText.visibility = View.GONE

        when {
            name.isEmpty() -> {
                updateErrorMessageEditText.text = getString(R.string.error_empty_name)
                updateErrorMessageEditText.visibility = View.VISIBLE
                return false
            }
            password != confirmPassword -> {
                updateErrorMessageEditText.text = getString(R.string.error_password_mismatch)
                updateErrorMessageEditText.visibility = View.VISIBLE
                return false
            }
            else -> {
                return true // Todos los campos son válidos
            }
        }
    }

    private fun updateUserData(userUpdate: UserUpdate){
        lifecycleScope.launch {
            loadingLayout.visibility = View.VISIBLE
            updateUserForm.visibility = View.GONE
            val response: Response<APIResponse<UserResponseItem>> = userService.updateUser(sharedPreferencesManager.getId(), userUpdate)
            if(response.isSuccessful){
               //Si la respuesta es correcta se actualizan los nuevos datos de forma local
                sharedPreferencesManager.updatePassword(passwordEditText.text.toString().trim()) //Datos extraidos de lo introducido por el usuario en el form
                sharedPreferencesManager.updateName(response.body()?.objectResponse?.name ?: nameEditText.text.toString().trim())
                showCorrectChanges()
            } else {
                loadingLayout.visibility = View.GONE
                updateUserForm.visibility = View.VISIBLE
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody,APIResponse::class.java)
                updateErrorMessageEditText.text = errorResponse?.message ?: getString(R.string.unknown_error)
                updateErrorMessageEditText.visibility = View.VISIBLE
            }
        }
    }
    //Rellena los datos del formulario por defecto con los datos actuales del usuario
    private fun fillFormWithUserCurrentData() {
        nameEditText.setText(sharedPreferencesManager.getName())
        passwordEditText.setText(sharedPreferencesManager.getPassword())
        confirmPasswordEditText.setText(sharedPreferencesManager.getPassword())
    }

    private fun showCorrectChanges(){
        findNavController().navigate(R.id.action_sectionUpdateUserFragment_to_sectionAccountFragment)
        Toast.makeText(requireContext(), getString(R.string.toast_successfully_updated), Toast.LENGTH_SHORT).show()
    }

}