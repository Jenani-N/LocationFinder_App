package com.example.locationfinder

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class database(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object{
        private const val DATABASE_NAME = "locationFinder.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "locationPosts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    //Creating the location table
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_ADDRESS TEXT, $COLUMN_LATITUDE TEXT, $COLUMN_LONGITUDE TEXT)"
        db?.execSQL(createTable)
    }

    //Upgrading a table
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    //Pulling all location postings to display on main
    fun getLocations(): List<location>{
        val locationPostings = mutableListOf<location>()
        val db = readableDatabase

        //Pull all entries from location table
        val query = "SELECT * FROM $TABLE_NAME"

        //Store it all in the cursor
        val cursor = db.rawQuery(query, null)

        //Go through the cursor iteratively to see all entries
        while(cursor.moveToNext()) {

            //Gets values from each column in row
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            val latitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE))
            val longitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))

            val Location = location(id, address, latitude, longitude)
            locationPostings.add(Location) //add location object to the list
        }
        cursor.close()
        db.close()
        return locationPostings
    }

    //Query feature to display lat & long for provided address
    fun getAddress(search: String): List<location>{
        val locationPostings = mutableListOf<location>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ADDRESS LIKE ?" //Get the matched address value
        val cursor = db.rawQuery(query, arrayOf("%$search%"))

        //Go through the cursor iteratively to see all addresses
        while(cursor.moveToNext()) {

            //Gets values from each column in row
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            val latitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE))
            val longitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))

            val Location = location(id, address, latitude, longitude)
            locationPostings.add(Location) //add location object to the list
        }

        cursor.close()
        db.close()
        return locationPostings
    }

    //Inserting a location posting into the database
    fun insertLocation(posting: location){
        val db = writableDatabase //to perform write operations for db

        //Storing data to insert into db
        val values = ContentValues().apply{

            //Adding the posting properties to the respective db column
            put(COLUMN_ADDRESS, posting.address)
            put(COLUMN_LATITUDE, posting.latitude)
            put(COLUMN_LONGITUDE, posting.longitude)

        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    //Pulling a location object using an ID
    fun getLocationByID(postingID: Int): location{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $postingID" //Find row with the given ID
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst() //Get first matched result

        //Retrieve the result row values/properties
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
        val latitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE))
        val longitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))

        cursor.close()
        db.close()
        return location(id, address, latitude, longitude)
    }


    //Updating an existing location posting
    fun updatePosting(posting: location){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, posting.address)
            put(COLUMN_LATITUDE, posting.latitude)
            put(COLUMN_LONGITUDE, posting.longitude)
        }
        val whereClause = "$COLUMN_ID = ?" //find posting with ID
        val whereArgs = arrayOf(posting.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs) //update query
        db.close()
    }
    //Permanently delete a posting
    fun deletePosting(postingID: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?" //find posting with ID
        val whereArgs = arrayOf(postingID.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs) //delete query
        db.close()
    }
}