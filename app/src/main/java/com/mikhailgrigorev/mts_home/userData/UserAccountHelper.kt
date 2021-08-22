package com.mikhailgrigorev.mts_home.userData

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class UserAccountHelper {
    companion object {
        fun login(sharedPreferences: SharedPreferences, view: View, login: String, password: String) {
            CoroutineScope(Dispatchers.IO).launch {
                val userRepo = UserRepository()
                val user = userRepo.getUserByLogin(login)
                if (user != null){
                    if(md5(password) == user.passwordHash)
                        sharedPreferences.edit()
                            .putLong("user_id", user.id!!)
                            .apply()
                    else{
                        Snackbar.make(view,"Wrong password", Snackbar.LENGTH_SHORT).show()
                    }
                }
                else{
                    Snackbar.make(view,"Wrong login", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        fun logout(sharedPreferences: SharedPreferences){
            CoroutineScope(Dispatchers.IO).launch {
                sharedPreferences.edit()
                    .putLong("user_id", -1L)
                    .apply()
            }
        }

        private fun md5(s: String): String {
            val MD5 = "MD5"
            try {
                // Create MD5 Hash
                val digest: MessageDigest = MessageDigest
                    .getInstance(MD5)
                digest.update(s.toByteArray())
                val messageDigest: ByteArray = digest.digest()

                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2) h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}