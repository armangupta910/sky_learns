package com.example.skylearns.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.skylearns.R
import com.example.skylearns.activities.leetcode


class coursesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val x = inflater.inflate(R.layout.fragment_courses, container, false)

        x.findViewById<CardView>(R.id.leetcode).setOnClickListener{
            startActivity(Intent(activity,leetcode::class.java))
        }
        return x
    }

}