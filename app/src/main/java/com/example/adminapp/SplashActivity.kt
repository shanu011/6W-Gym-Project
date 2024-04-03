package com.example.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            if(auth.currentUser?.uid != null) {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        },5000)
    }
}