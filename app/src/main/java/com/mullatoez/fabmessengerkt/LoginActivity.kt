package com.mullatoez.fabmessengerkt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mullatoez.fabmessengerkt.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backRegisterTv.setOnClickListener {
            finish()
        }

        binding.loginBtn.setOnClickListener {

            val email = binding.emailEdittextLogin.text.toString()
            val password = binding.passwordEdittextLogin.text.toString()

            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{

                }
                .addOnFailureListener{

                }

        }

    }
}