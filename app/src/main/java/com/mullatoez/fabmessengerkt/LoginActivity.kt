package com.mullatoez.fabmessengerkt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    Toast.makeText(this, "Login was successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    startActivity(intent)

                }
                .addOnFailureListener {

                    Toast.makeText(this, "Login Failed: " + it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

        }

    }
}