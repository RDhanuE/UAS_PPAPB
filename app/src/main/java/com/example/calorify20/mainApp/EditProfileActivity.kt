package com.example.calorify20.mainApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.dataClass.Users
import com.example.calorify20.databinding.ActivityEditProfileBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var current_user = LogRegScreen.auth.currentUser!!
        var current_user_data = LogRegScreen.firestore.collection("users").document(current_user.uid)
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        var selectedDate = ""
        var email = ""
        var isAdmin = false
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        with(binding){
            current_user_data.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val olusername = documentSnapshot.getString("username")
                    val olheight = documentSnapshot.getLong("current_height")
                    val olweight = documentSnapshot.getLong("current_weight")
                    val olgoal_weight = documentSnapshot.getLong("goal_weight")
                    val olcalory = documentSnapshot.getLong("goal_calories")
                    val oldate = documentSnapshot.getString("goal_date")
                    email = documentSnapshot.getString("email").toString()
                    isAdmin = documentSnapshot.getBoolean("isAdmin") == true

                    profileEditUsername.setText(olusername)
                    profileEditCurrentHeight.setText(olheight.toString())
                    profileEditCurrentWeight.setText(olweight.toString())
                    profileEditGoalWeight.setText(olgoal_weight.toString())
                    profileEditCalory.setText(olcalory.toString())
                    profileEditDatePicker.setText(oldate)
                }
            }

            profileEditDatePicker.setOnClickListener{
                datePicker.addOnPositiveButtonClickListener{
                    selectedDate = simpleDateFormat.format(datePicker.selection)
                    profileEditDatePicker.setText(selectedDate)
                }
                datePicker.show(supportFragmentManager, "DatePicker")
            }

            profileEditButton.setOnClickListener {
                val username = profileEditUsername.text.toString()
                val currWeight = profileEditCurrentWeight.text.toString()
                val currHeight = profileEditCurrentHeight.text.toString()
                val goalsWeight = profileEditGoalWeight.text.toString()
                val calories = profileEditCalory.text.toString()
                var date = profileEditDatePicker.text.toString()
                profileEditUsernameContainer.error = null
                profileEditCurrentHeightContainer.error = null
                profileEditCurrentWeightContainer.error = null
                profileEditGoalWeightContainer.error = null
                profileEditCaloryContainer.error = null
                profileEditDatePickerContainer.error = null
                if (username.isNullOrEmpty()) {
                    profileEditUsernameContainer.error = "Username Required"
                } else if (currHeight.isNullOrEmpty()) {
                    profileEditCurrentHeightContainer.error = "Current Weight Required"
                } else if (currWeight.isNullOrEmpty()) {
                    profileEditCurrentWeightContainer.error = "Current Weight Required"
                } else if (goalsWeight.isNullOrEmpty()) {
                    profileEditGoalWeightContainer.error = "Goal's Weight Required"
                } else if (calories.isNullOrEmpty()) {
                    profileEditCaloryContainer.error = "Daily Calory Goals Required"
                } else if (date.isNullOrEmpty()) {
                    profileEditDatePickerContainer.error = "Goal's Date Required"
                } else {
                    var user = Users(
                        username = username,
                        current_weight = currWeight.toString().toInt(),
                        current_height = currHeight.toString().toInt(),
                        goal_weight = goalsWeight.toString().toInt(),
                        goal_date = date,
                        goal_calories = calories.toString().toInt(),
                        email = email,
                        isAdmin = isAdmin
                    )
                    current_user_data.set(user).addOnSuccessListener {
                        Toast.makeText(this@EditProfileActivity, "Edit data succesfull", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}