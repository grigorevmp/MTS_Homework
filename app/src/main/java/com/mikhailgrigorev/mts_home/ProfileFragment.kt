package com.mikhailgrigorev.mts_home

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.mikhailgrigorev.mts_home.mvvm.UserViewModel
import com.mikhailgrigorev.mts_home.userData.Encryption
import com.mikhailgrigorev.mts_home.userData.User
import com.mikhailgrigorev.mts_home.userData.UserAccountHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment: Fragment()  {
    private val userViewModel: UserViewModel by viewModels()
    private val progressDialog by lazy {
        ProgressDialog.show(
            this.context,
            "",
            getString(R.string.please_wait)
        )
    }

    private lateinit var userLogin: TextView
    private lateinit var accountMail: TextView
    private lateinit var editTextTextPersonName: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var editTextTextEmailAddress: EditText
    private lateinit var editTextPhone: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        userLogin = view.findViewById(R.id.userLogin)
        accountMail = view.findViewById(R.id.accountMail)
        editTextTextPersonName = view.findViewById(R.id.editTextTextPersonName)
        editTextTextPassword = view.findViewById(R.id.editTextTextPassword2)
        editTextTextEmailAddress = view.findViewById(R.id.editTextTextEmailAddress)
        editTextPhone = view.findViewById(R.id.editTextPhone)

        val sharedPreferences = this.context?.let { Encryption.getSecretSharedPref(it) }
        val userId = sharedPreferences?.getLong("user_id", -1)
        if (userId == -1L){
            view = inflater.inflate(R.layout.fragment_profile_unlogged, container, false)
            val enterButton = view.findViewById<MaterialButton>(R.id.enterButton)
            enterButton.setOnClickListener {
                val login = view.findViewById<TextInputEditText>(R.id.login)
                val password = view.findViewById<TextInputEditText>(R.id.password)
                UserAccountHelper.login(sharedPreferences, view,
                    login.text.toString(),
                    password.text.toString()
                )
                refreshCurrentFragment(this)
            }
        }
        else{
            userViewModel.currentUser.observe(viewLifecycleOwner, Observer(::setDataToView))
            userViewModel.viewState.observe(viewLifecycleOwner, Observer(::render))

            userViewModel.loadUser(userId!!)

            val logout = view.findViewById<MaterialButton>(R.id.logout)
            logout.setOnClickListener {
                UserAccountHelper.logout(sharedPreferences)
                refreshCurrentFragment(this)
            }
        }

        return view
    }

    private fun setDataToView(user: User){

        userLogin.text = user.login
        accountMail.text = user.email
        editTextTextPersonName.setText(user.name)
        editTextTextPassword.setText(user.passwordHash)
        editTextTextEmailAddress.setText(user.email)
        editTextPhone.setText(user.phone)
    }

    private fun refreshCurrentFragment(fragment: Fragment){
        val navController = findNavController(fragment)

        val id = navController.currentDestination?.id
        navController.popBackStack(id!!,true)
        navController.navigate(id)
    }


    private fun render(viewState: ViewState) = with(viewState) {
        if (isDownloaded) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    data class ViewState(
        val isDownloaded: Boolean = false
    )



}
