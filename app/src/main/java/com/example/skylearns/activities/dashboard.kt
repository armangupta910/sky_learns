package com.example.skylearns.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.skylearns.R
import com.example.skylearns.fragments.coursesFragment
import com.example.skylearns.fragments.profileragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class dashboard : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private fun checkAndSetUserData(userId: String) {
        val userRef = database.child("Users").child(userId)

        // Check if the user exists
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // UserID exists
                    println("UserID $userId already exists.")
                } else {
                    // UserID does not exist, set the data
                    val userData = mapOf(
                        "LeetCode1" to "0",
                        "LeetCode2" to "0",
                        "LeetCode3" to "0",
                        "LeetCode4" to "0"
                    )

                    userRef.setValue(userData)
                        .addOnSuccessListener {
                            // Data set successfully
                            println("User data set successfully.")
                        }
                        .addOnFailureListener { error ->
                            // Error occurred

                            println("Error setting user data: ${error.message}")
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Error occurred while reading data
                println("Error checking user data: ${databaseError.message}")
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        database = FirebaseDatabase.getInstance().reference
        checkAndSetUserData(FirebaseAuth.getInstance().currentUser?.uid.toString())

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, coursesFragment())
            .addToBackStack(null)
            .commit()

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_courses -> {
                    val fragment = coursesFragment()
                    openFragment(fragment)
                    true
                }
                R.id.navigation_profile -> {
                    val fragment = profileragment()
                    openFragment(fragment)
                    true
                }
                else -> false
            }
        }

        // Set default fragment
        navView.selectedItemId = R.id.navigation_courses
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}