package com.example.locationfinder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//Defining the each location object's attributes
data class location(val id:Int, val address:String, val latitude:String, val longitude:String ){

}