package com.martingago.blog_retrofit.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.controller.UserController
import com.martingago.blog_retrofit.model.roles.RoleResponseItem
import com.martingago.blog_retrofit.model.roles.RolesRequestItem
import com.martingago.blog_retrofit.model.user.UserResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

class UpdateUserRolesFragment : Fragment() {

    private lateinit var userController: UserController
    private lateinit var tagUsername: TextView
    private lateinit var profileUsernameText: TextView
    private lateinit var profileNameText: TextView
    private lateinit var profileRolesText: TextView
    private lateinit var btnOpenRolesSelector: Button
    private lateinit var btnUpdateUserRoles: Button
    private lateinit var selectedRoleIds: RolesRequestItem
    private lateinit var allRoles: List<RoleResponseItem>

    private lateinit var loaderSection : View
    private lateinit var loaderMsg : TextView
    private lateinit var userSection : View


    private val args: UpdateUserRolesFragmentArgs by navArgs()
    private lateinit var user: UserResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = args.userData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_user_roles, container, false)
        userController = UserController(requireContext())
        tagUsername = view.findViewById(R.id.tagUsername)
        profileUsernameText = view.findViewById(R.id.profileUsername)
        profileNameText = view.findViewById(R.id.profileName)
        profileRolesText = view.findViewById(R.id.profileRoles)
        btnOpenRolesSelector = view.findViewById(R.id.btnOpenRolesSelector)
        btnUpdateUserRoles = view.findViewById(R.id.btnUpdateRoles)

        //Loaders
        loaderSection = view.findViewById(R.id.loaderSection)
        loaderMsg = view.findViewById(R.id.loadingMsg)
        userSection = view.findViewById(R.id.userSection)

        // Inicializamos selectedRoleIds con los IDs de los roles actuales
        selectedRoleIds = RolesRequestItem(mutableListOf())
        allRoles = listOf(
            RoleResponseItem(1, "USER"),
            RoleResponseItem(2, "ADMIN"),
            RoleResponseItem(3, "EDITOR")
        )

        user.userRoles.forEach { roleName ->
            allRoles.find { it.role == roleName }?.let { role ->
                selectedRoleIds.roles.add(role.id)
            }
        }

        updateRoleTextView()
        btnOpenRolesSelector.setOnClickListener { openRolesSelector() }
        btnUpdateUserRoles.setOnClickListener { updateUserRoles() }

        setCardUserDetails()
        return view
    }

    private fun setCardUserDetails() {
        profileUsernameText.text = user.username
        profileNameText.text = user.name
    }

    private fun openRolesSelector() {
        val roleNames = allRoles.map { it.role }.toTypedArray()
        val selected = BooleanArray(allRoles.size) { index ->
            selectedRoleIds.roles.contains(allRoles[index].id)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Select Roles")
            .setMultiChoiceItems(roleNames, selected) { _, which, isChecked ->
                val role = allRoles[which]
                if (isChecked) {
                    if (!selectedRoleIds.roles.contains(role.id)) {
                        selectedRoleIds.roles.add(role.id)
                    }
                } else {
                    selectedRoleIds.roles.remove(role.id)
                }
            }
            .setPositiveButton("OK") { dialog, _ ->
                updateRoleTextView()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateRoleTextView() {
        val selectedRoleNames = allRoles.filter { selectedRoleIds.roles.contains(it.id) }
            .joinToString(", ") { it.role }
        profileRolesText.text = "Roles: $selectedRoleNames"
    }

    private fun updateUserRoles() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {

                val response = withContext(Dispatchers.IO){
                    userController.updateUserRoles(
                        user.id,
                        selectedRoleIds,
                        updateStatus = {currentAttempt, maxAttempts ->
                            showLoader(currentAttempt, maxAttempts)
                        }
                    )
                }
                if(response.success == true){
                    showMessage(response.message)
                    findNavController().navigate(UpdateUserRolesFragmentDirections.actionUpdateUserRolesFragmentToSectionManageUsersFragment())
                }else{
                    showMessage(response.message)
                    hiddeLoader()
                }


            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Failed to update user roles: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoader(currentAttempt : Int, maxAttempt : Int){
        loaderSection.visibility = View.VISIBLE
        loaderMsg.text = getString(R.string.loading_updating_roles, currentAttempt, maxAttempt)
        userSection.visibility = View.GONE
    }

    private fun hiddeLoader(){
        loaderSection.visibility = View.GONE
        userSection.visibility = View.VISIBLE
    }

    private fun showMessage(message : String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
