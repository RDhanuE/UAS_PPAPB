package com.example.calorify20.mainApp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.databinding.FragmentHomeBinding
import com.example.calorify20.recyclerView.ActivityRvAdapter
import com.example.calorify20.room.CalorifyDatabase
import com.example.calorify20.room.InOutTakeDao
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var mActivityDao: InOutTakeDao
    lateinit var executorService: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()
        val db = CalorifyDatabase.getDatabase(this.requireActivity())
        mActivityDao = db!!.InOutTakeDao()!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var current_user = LogRegScreen.auth.currentUser!!
        var current_user_data = LogRegScreen.firestore.collection("users").document(current_user.uid)
        var userCalories : Int = 0

        mActivityDao.getRecentActivtiyByCurrentUser(current_user.uid.toString()).observe(this.requireActivity()){
            binding.homeRecentRv.apply {
                adapter = ActivityRvAdapter(it)
                layoutManager = LinearLayoutManager(this@HomeFragment.requireActivity())
            }
        }

        current_user_data.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                userCalories = documentSnapshot.getLong("goal_calories").toString().toInt()
                mActivityDao.getCaloryByType(current_user.uid.toString(), "Intake").observe(this.requireActivity()){intake ->
                    mActivityDao.getCaloryByType(current_user.uid.toString(), "Outtake").observe(this.requireActivity()) {outtake ->
                        val calories = intake - outtake
                        if (userCalories - calories < 0) {
                            binding.homeDailyTextBig.setText("0")
                        } else {
                            binding.homeDailyTextBig.setText((userCalories - calories).toString())
                        }
                    }
                }
            }
        }
    }
}