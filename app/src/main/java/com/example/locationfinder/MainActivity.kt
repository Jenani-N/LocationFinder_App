package com.example.locationfinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var db: database
    private lateinit var binding: ActivityMainBinding
    private lateinit var postAdapter: locationAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = database(this)
        postAdapter = locationAdapter(db.getLocations(), this) //display all current postings

        binding.postRecyclerView.layoutManager = LinearLayoutManager(this) //display the postings vertically
        binding.postRecyclerView.adapter = postAdapter

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Link action function to the create posting button
        binding.newPostButton.setOnClickListener{
            onAddPost(it)
        }

        //Run search function whenever user types something in the search query bar
        binding.lookupView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(search: String?): Boolean {
                searchAddress(search)
                return true
            }
        })
    }

    // Action for the + floating icon button -> creating a new post
    fun onAddPost(view: View?){
        val intent = Intent(this, newLocation::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        postAdapter.refresh(db.getLocations())
    }

    // Searching for longitude & latitude with an address
    fun searchAddress(searchA: String?){
        if(searchA.isNullOrEmpty()){
            postAdapter.refresh(db.getLocations()) //If search bar is null, display all postings
        }
        else
        {
            postAdapter.refresh(db.getAddress(searchA)) //Only display matched address postings
        }
    }
}