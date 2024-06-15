package com.example.skylearns.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.skylearns.R
import com.example.skylearns.activities.login_page
import com.google.firebase.auth.FirebaseAuth

class profileragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        val x = inflater.inflate(R.layout.fragment_profileragment, container, false)
        val butt:Button = x.findViewById(R.id.logOut)
        butt.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context,login_page::class.java))
            activity?.finish()
        }
        return x
    }

}