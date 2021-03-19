package com.mullatoez.fabmessengerkt

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mullatoez.fabmessengerkt.databinding.ActivityRegisterBinding
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectPhotoBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.registerButton.setOnClickListener {

            performRegister()

        }

        binding.haveAccountTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    var selectedUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedUri)
            binding.selectPhotoImageView.setImageBitmap(bitmap)
            binding.selectPhotoBtn.alpha = 0f
            /* val bitmapDrawable = BitmapDrawable(bitmap)
             binding.selectPhotoBtn.setBackgroundDrawable(bitmapDrawable)*/
        }
    }

    private fun performRegister() {

        val email = binding.emailEdittext.text.toString()
        val password = binding.passwordEdittext.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this, "Please enter all required details", Toast.LENGTH_SHORT
            ).show()
            return
        }

        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                Toast.makeText(
                    this, "Successfully created a user", Toast.LENGTH_SHORT
                ).show()

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "Failed to login the user" + it.localizedMessage, Toast.LENGTH_SHORT
                ).show()
                return@addOnFailureListener
            }
    }

    private fun uploadImageToFirebaseStorage() {

        if (selectedUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedUri!!)
            .addOnSuccessListener {
                Toast.makeText(this, "Upload was successful", Toast.LENGTH_SHORT).show()

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToRealtimeDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Upload failed " + it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun saveUserToRealtimeDatabase(profileImageUrl: String) {

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val dbase = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, binding.usernameEdittext.text.toString(), profileImageUrl)

        dbase.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data saved successful", Toast.LENGTH_SHORT).show()
            }
    }
}

class User(val uid: String, val userName: String, val profileImageUrl: String)