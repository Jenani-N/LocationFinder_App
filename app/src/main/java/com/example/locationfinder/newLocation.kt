package com.example.locationfinder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.locationfinder.databinding.ActivityNewLocationBinding

class newLocation : AppCompatActivity() {

    private lateinit var binding: ActivityNewLocationBinding
    private lateinit var db: database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = database(this)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Action for the save posting button
        binding.saveButton.setOnClickListener {
            onSave(it)
        }

        //Action for the back button
        binding.backButton.setOnClickListener {
            onBack(it)
        }
    }

    //Function for saving the note
    fun onSave(view: View?){
        val address = binding.editTextAddress.text.toString()
        val latitude = binding.editTextLatitude.text.toString()
        val longitude = binding.editTextLongitude.text.toString()

        //Confirm that all information has been filled out
        if(address != "" || latitude != "" || longitude != ""){
            val post = location(0, address, latitude, longitude)
            db.insertLocation(post) //insert into db
            finish()
            Toast.makeText(this, "Post Successfully Saved", Toast.LENGTH_SHORT).show() //Confirmation pop up message

            //Go back to the homepage
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)

        }else{
            Toast.makeText(applicationContext, "Please fill out all fields.", Toast.LENGTH_LONG).show() //Error pop up message
        }
    }

    //Function for clicking the back button
    fun onBack(view: View?){
        //Without saving anything, go back to the homepage
        val intent3 = Intent(this, MainActivity::class.java)
        startActivity(intent3)
    }
}