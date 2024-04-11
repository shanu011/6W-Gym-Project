 package com.example.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.adminapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

 class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var navController: NavController
    var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.fragment)
        setSupportActionBar(binding.toolBar)
    }

     override fun onResume() {
         super.onResume()

         binding.toolBar.title = "Yoga"

         binding.toolBar.title = "YOGA"
     }

     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.menu_list,menu)
         return true
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when(item.itemId){
             R.id.logout->{

               var dialog = AlertDialog.Builder(this)
                 dialog.setCancelable(false)
                 dialog.setTitle("Logout")
                 dialog.setMessage("Do You Want To Logout")
                 dialog.setPositiveButton("Yes"){_,_->
                     auth.signOut()

                 }
                 dialog.setNegativeButton("No"){_,_->

                 }
                 dialog.show()
             }
         }
         return super.onOptionsItemSelected(item)

     }
}