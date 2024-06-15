package com.example.skylearns.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skylearns.R
import com.example.skylearns.activities.login_page.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class leetcode : AppCompatActivity() {
    private lateinit var database1: DatabaseReference
    private fun modifyUserData(userId: String,leet:String,status:String) {
        val userRef = database1.child("Users").child(userId)

        // Read the existing data
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // UserID exists, read the current data
                    val userData = dataSnapshot.value as Map<String, Any>
                    println("Current user data: $userData")

                    // Modify the data (example: increment values by 1)
                    val modifiedData = userData.toMutableMap()
                    modifiedData[leet] = status

                    // Update the data in Firebase
                    userRef.setValue(modifiedData)
                        .addOnSuccessListener {
                            // Data updated successfully
                            println("User data updated successfully.")
                            Log.d(TAG,"You've watched the video!")
                        }
                        .addOnFailureListener { error ->
                            // Error occurred
                            println("Error updating user data: ${error.message}")
                        }
                } else {
                    // UserID does not exist
                    println("UserID $userId does not exist.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Error occurred while reading data

                println("Error reading user data: ${databaseError.message}")
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leetcode)
        database1 = FirebaseDatabase.getInstance().reference
        val database = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Users")


        findViewById<ImageView>(R.id.goBack).setOnClickListener {
            finish()
        }


        val userRef = database1.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
        // Read the existing data
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // UserID exists, read the current data
                    val userData = dataSnapshot.value as Map<String, Any>
                    println("Current user data: $userData")
                    if(userData["LeetCode4"] == "1") {
                        findViewById<TextView>(R.id.text4).setTextColor(resources.getColor(R.color.black))
                    }
                    if(userData["LeetCode3"] == "1") {
                        findViewById<TextView>(R.id.text3).setTextColor(resources.getColor(R.color.black))
                    }
                    if(userData["LeetCode2"] == "1") {
                        findViewById<TextView>(R.id.text2).setTextColor(resources.getColor(R.color.black))
                    }
                    if(userData["LeetCode1"] == "1") {
                        findViewById<TextView>(R.id.text1).setTextColor(resources.getColor(R.color.black))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



        findViewById<CardView>(R.id.l1).setOnClickListener{
            modifyUserData(FirebaseAuth.getInstance().currentUser?.uid.toString(),"LeetCode4","1")

            findViewById<TextView>(R.id.text4).setTextColor(resources.getColor(R.color.black,null))

            startActivity(Intent(this,video::class.java))
        }
        findViewById<CardView>(R.id.l2).setOnClickListener{
            modifyUserData(FirebaseAuth.getInstance().currentUser?.uid.toString(),"LeetCode3","1")

            findViewById<TextView>(R.id.text3).setTextColor(resources.getColor(R.color.black,null))

            startActivity(Intent(this,video::class.java))
        }
        findViewById<CardView>(R.id.l3).setOnClickListener{
            modifyUserData(FirebaseAuth.getInstance().currentUser?.uid.toString(),"LeetCode2","1")

            findViewById<TextView>(R.id.text2).setTextColor(resources.getColor(R.color.black,null))

            startActivity(Intent(this,video::class.java))
        }
        findViewById<CardView>(R.id.l4).setOnClickListener{
            modifyUserData(FirebaseAuth.getInstance().currentUser?.uid.toString(),"LeetCode1","1")

            findViewById<TextView>(R.id.text1).setTextColor(resources.getColor(R.color.black,null))

            startActivity(Intent(this,video::class.java))
        }
    }
}