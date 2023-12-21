package com.example.calorify20.auth

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LogRegTabAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity){
    private val fragmentArray = arrayOf<Fragment>(RegisterFragment(), LoginFragment())

    override fun getItemCount(): Int {
        return fragmentArray.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentArray[position]
    }
}