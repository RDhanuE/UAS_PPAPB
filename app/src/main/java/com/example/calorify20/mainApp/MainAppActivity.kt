package com.example.calorify20.mainApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.calorify20.R
import com.example.calorify20.databinding.ActivityMainAppBinding
import com.example.calorify20.room.CalorifyDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.Executors


class MainAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityMainAppBinding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = CalorifyDatabase.getDatabase(this@MainAppActivity)
        val mActivityDao = db!!.InOutTakeDao()!!
        val executorService = Executors.newSingleThreadExecutor()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        if (PrefManager.getInstance(this@MainAppActivity).getSavedDate() != dateFormat){
            executorService.execute{
                mActivityDao.clearActivity()
            }
            PrefManager.getInstance(this@MainAppActivity).saveCurrentDate(Calendar.getInstance().time)
        }


        with(binding) {
            val navController = findNavController(R.id.main_App_Fragment)
            mainAppBotNav.setupWithNavController(navController)
        }
    }
}