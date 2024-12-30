package com.martingago.blog_retrofit.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.controller.UserController
import com.martingago.blog_retrofit.interfaces.UserLoginStatusChangeListener
import com.martingago.blog_retrofit.model.tag.TagResponseItem
import com.martingago.blog_retrofit.network.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SectionAccountFragment : Fragment() {

    private  lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var  userController: UserController
    private lateinit var tagUsername : TextView
    private lateinit var profileUsernameText : TextView
    private lateinit var profileNameText : TextView
    private lateinit var profileRolesText : TextView
    private lateinit var btnDeleteAccount : Button
    private lateinit var btnNavigateUpdateUser : Button

    //Loader
    private  lateinit var loadingSection : View
    private lateinit var loadingMsg : TextView
    private lateinit var userSection : View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_section_account, container, false)

        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        userController = UserController(requireContext())

        tagUsername = view.findViewById(R.id.tagUsername)
        profileUsernameText = view.findViewById(R.id.profileUsername)
        profileNameText = view.findViewById(R.id.profileName)
        profileRolesText = view.findViewById(R.id.profileRoles)
        btnNavigateUpdateUser = view.findViewById(R.id.btnNavigateToUpdateUser)
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount)
        loadingSection = view.findViewById(R.id.loaderSection)
        loadingMsg = view.findViewById(R.id.loadingMsg)
        userSection = view.findViewById(R.id.userSection)

        setCardUserDetails() // inicializa los datos del usuario en la card
        loadLogoutFragment()
        //Muestra popup confirmacion eliminacion perfil
        btnDeleteAccount.setOnClickListener { popupConfirmDeleteAccount() }

        //Navega a la section de actualizar usuario
        btnNavigateUpdateUser.setOnClickListener { navigateToUpdateUser() }

        return view

    }

    /**
     * Función que muestra un Dialog de confirmación cuando el usuario quiere eliminar una tag.
     * Además también muestra un loader durante el proceso de eliminación de datos.
     * Una vez eliminada la tag, se actualiza la lista local de tags.
     */
    private fun popupConfirmDeleteAccount() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_tag))
        builder.setMessage(getString(R.string.message_confirm_delete_user, sharedPreferencesManager.getUsername()))

        // Código para el boton de confirmar, se llama al controller que gestiona eliminación de tags y se muestra un loader
        builder.setPositiveButton("Sí") { dialog, _ ->
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    //Muestra el dialog parametrizado
                    showLoaderDelete(sharedPreferencesManager.getUsername() ?: "Unknown user")
                    val response = withContext(Dispatchers.IO) {
                        userController.deleteUserById(sharedPreferencesManager.getId())
                    }
                    //Muestra mensaje personalizado en base a la respuesta del servidor
                    if (response.success == true) {
                        sharedPreferencesManager.clearCredentials()
                        // Notificar a la actividad para que actualice el menú
                        (activity as? UserLoginStatusChangeListener)?.updateMenuBasedOnLoginStatus()
                        navigateToRegisterUser()
                        hideLoader()
                        Toast.makeText(requireContext(), getString(R.string.delete_user_confirm), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.delete_user_error), Toast.LENGTH_SHORT).show()
                        hideLoader()
                    }
                    hideLoader()
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.delete_tag_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Cerrar el diálogo
        }
        // Mostrar el diálogo
        builder.create().show()
    }

    private fun setCardUserDetails(){
        val tagID = sharedPreferencesManager.getUsername()?.first()?.toUpperCase() ?: "?" //Obtiene inicial mayucula usuario
        tagUsername.text = tagID.toString() //Establece inicial tagUser

        val usernameData = getString(R.string.username_label, sharedPreferencesManager.getUsername() ?: "Unknown")
        val nameData = getString(R.string.name_label, sharedPreferencesManager.getName() ?: "Unknown")
        val roles = sharedPreferencesManager.getRoles() ?: emptyList()
        val rolesString = roles.joinToString(separator = ", ")
        val rolesData = getString(R.string.roles_label, if (roles.isEmpty()) "No roles" else rolesString)

        profileUsernameText.text = usernameData
        profileNameText.text = nameData
        profileRolesText.text = rolesData

    }

    private fun loadLogoutFragment(){
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        val logoutFragment = LogoutFragment()
        transaction.replace(R.id.fragmentLogoutUser, logoutFragment)
        transaction.commit()
    }
    private fun navigateToUpdateUser() {
        // Navegar usando el NavController
        findNavController().navigate(R.id.action_sectionAccountFragment_to_sectionUpdateUserFragment)
    }

    private  fun navigateToRegisterUser(){
        findNavController().navigate(R.id.action_sectionAccountFragment_to_sectionRegisterFragment)
    }


    private fun hideLoader() {
        loadingSection.visibility = View.GONE
        userSection.visibility = View.VISIBLE
    }

    private fun showLoaderDelete(name: String) {
        loadingSection.visibility = View.VISIBLE
        userSection.visibility = View.GONE
        loadingMsg.text = getString(R.string.loading_deleting_user, name)
    }
}