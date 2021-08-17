package com.mikhailgrigorev.mts_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.mikhailgrigorev.mts_home.userData.Encryption
import com.mikhailgrigorev.mts_home.userData.UserAccountHelper
import com.mikhailgrigorev.mts_home.userData.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment: Fragment()  {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            val userRepo = UserRepository(view.context)
        }

        val sharedPreferences = this.context?.let { Encryption.getSecretSharedPref(it) }
        val userId = sharedPreferences?.getLong("user_id", -1)
        if (userId == -1L){
            view = inflater.inflate(R.layout.fragment_profile_unlogged, container, false)
            val enterButton = view.findViewById<MaterialButton>(R.id.enterButton)
            enterButton.setOnClickListener {
                val login = view.findViewById<TextInputEditText>(R.id.login)
                val password = view.findViewById<TextInputEditText>(R.id.password)
                UserAccountHelper.login(view.context,
                    sharedPreferences, view, login.text.toString(),  password.text.toString())
                refreshCurrentFragment(this)
            }
        }
        else{
            CoroutineScope(Dispatchers.IO).launch {
                setDataToView(view, userId!!)
            }

            val logout = view.findViewById<MaterialButton>(R.id.logout)
            logout.setOnClickListener {
                UserAccountHelper.logout(sharedPreferences!!)
                refreshCurrentFragment(this)
            }
        }

        return view
    }

    private fun setDataToView(view: View, userId: Long){
        val userLogin = view.findViewById<TextView>(R.id.userLogin)
        val accountMail = view.findViewById<TextView>(R.id.accountMail)
        val editTextTextPersonName =
            view.findViewById<EditText>(R.id.editTextTextPersonName)
        val editTextTextPassword = view.findViewById<EditText>(R.id.editTextTextPassword2)
        val editTextTextEmailAddress =
            view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val editTextPhone = view.findViewById<EditText>(R.id.editTextPhone)

        val userRepo = UserRepository(view.context)
        val user = userRepo.getUserById(userId)
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



}

