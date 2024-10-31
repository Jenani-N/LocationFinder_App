package com.example.locationfinder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.example.locationfinder.databinding.ActivityUpdateLocationBinding

class updateLocation : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateLocationBinding
    private lateinit var db: database
    private var postingID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateLocationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = database(this)

        //Get the post's ID
        postingID = intent.getIntExtra("postingID", -1)

        //Check for invalid ID
        if (postingID == -1){
            finish()
            return
        }

        /* in update location xml put id for the editText (user input for post) as the below */

        val post = db.getLocationByID(postingID)
        binding.updateAddressEditText.setText(post.address)
        binding.updateLatitudeEditText.setText(post.latitude)
        binding.updateLongitudeEditText.setText(post.longitude)

        //Action for the back button
        binding.updateBackButton.setOnClickListener {
            onBack(it)
        }


        binding.updateSaveButton.setOnClickListener{

            //Get updated fields (user input)
            val updatedAddress = binding.updateAddressEditText.text.toString()
            val updatedLatitude = binding.updateLatitudeEditText.text.toString()
            val updatedLongitude = binding.updateLongitudeEditText.text.toString()

            //Check that all fields are filled
            if (updatedAddress.isBlank() || updatedLatitude.isBlank() || updatedLongitude.isBlank()){
                Toast.makeText(this,"Please fill out all fields.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //Update properties of the same posting ID
            val updatedPost = location(postingID, updatedAddress, updatedLatitude, updatedLongitude)
            db.updatePosting(updatedPost) //update the database entry

            val intent = Intent()
            intent.putExtra("isUpdated", true)
            setResult(RESULT_OK, intent)
            finish()
            Toast.makeText(this, "Changes Successfully Saved", Toast.LENGTH_SHORT).show() //confirmation pop up message
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //Function for clicking the back button
    fun onBack(view: View?){
        //Without saving anything, go back to the homepage
        val intent3 = Intent(this, MainActivity::class.java)
        startActivity(intent3)
    }
}