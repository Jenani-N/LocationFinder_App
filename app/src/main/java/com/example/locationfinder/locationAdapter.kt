package com.example.locationfinder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView


class locationAdapter(private var post: List<location>, context: Context) : RecyclerView.Adapter<locationAdapter.postViewHolder>() {

    private val db: database = database(context)

    //Get all user input and view values **RENAME VIEW ID'S TO MATCH**
    class postViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val latitudeTextView: TextView = itemView.findViewById(R.id.latitudeTextView)
        val longitudeTextView: TextView = itemView.findViewById(R.id.longitudeTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }


    //Creating a new view for each location posting
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_location_adapter, parent, false)
        return postViewHolder(view)
    }

    //Number of postings to be displayed
    override fun getItemCount(): Int = post.size

    //Updates the list of displayed postings
    fun refresh(newPost: List<location>){
        post = newPost
        notifyDataSetChanged()
    }


    //Inserting the information onto the postings
    override fun onBindViewHolder(holder: postViewHolder, position: Int) {
        val post = post[position] //get location posting
        if (post != null) {

            //Display the posting properties onto the textView elements
            holder.addressTextView.text = post.address
            holder.latitudeTextView.text = post.latitude
            holder.longitudeTextView.text = post.longitude

            //Action for update button/icon
            holder.updateButton.setOnClickListener {
                val intent = Intent(holder.itemView.context, updateLocation::class.java).apply {
                    putExtra("postingID", post.id)
                }
                holder.itemView.context.startActivity(intent)
            }
        }

        //Action for delete button/icon
        holder.deleteButton.setOnClickListener{
            db.deletePosting(post.id)
            refresh(db.getLocations())
            Toast.makeText(holder.itemView.context, "Posting has been Deleted", Toast.LENGTH_SHORT).show() //confirmation pop up message
        }

    }

}