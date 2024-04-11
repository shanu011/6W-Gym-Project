package com.example.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.adminapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    var auth = Firebase.auth
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+/.+[a-z]+"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {

            if(binding.etEmail.text.toString().isEmpty()){
                binding.etEmail.error = "Enter Your Email"
            }else if(binding.etEmail.text.matches(emailPattern.toRegex())){
                binding.etEmail.error = "Enter Your Valid Email"
            }
            else if(binding.etPassword.text.toString().isEmpty()){
                binding.etPassword.error = "Enter Your Password"
            }else{
                auth.signInWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        var intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        this.finish()
                        Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"Your Credential is wrong",Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    println("Error: ${it.message}")
                }
            }
        }
    }
}