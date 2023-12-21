package com.example.calorify20.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calorify20.dataClass.Users
import com.example.calorify20.databinding.ActivityInitializeScreenBinding
import com.example.calorify20.mainApp.MainAppActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class InitializeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityInitializeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitializeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent?.extras
        val currentUser = LogRegScreen.auth.currentUser
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var selectedDate = ""

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val usersCollectionReference = LogRegScreen.firestore.collection("users")


        with(binding) {
            initUsername.error = null
            initCurrentHeight.error = null
            initCurrentWeight.error = null
            initGoalWeight.error = null
            initCalory.error = null
            initDatePicker.error = null

            initUsername.setText(data?.getString("USERNAME"))

            initDatePicker.setOnClickListener{
                datePicker.addOnPositiveButtonClickListener{
                    selectedDate = simpleDateFormat.format(datePicker.selection)
                    initDatePicker.setText(selectedDate)
                }
                datePicker.show(supportFragmentManager, "DatePicker")
            }

            initBackButton.setOnClickListener{
                finish()
            }

            initButton.setOnClickListener{
                val username = initUsername.text.toString()
                val currWeight = initCurrentWeight.text.toString()
                val currHeight = initCurrentHeight.text.toString()
                val goalsWeight = initGoalWeight.text.toString()
                val calories = initCalory.text.toString()
                var date = initDatePicker.text.toString()
                initUsernameContainer.error = null
                initCurrentHeight.error = null
                initCurrentWeight.error = null
                initGoalWeightContainer.error = null
                initCaloryContainer.error = null
                initDatePickerContainer.error = null
                if (username.isNullOrEmpty()) {
                    initUsernameContainer.error = "Username Required"
                } else if (currHeight.isNullOrEmpty()) {
                    initCurrentWeightContainer.error = "Current Weight Required"
                } else if (currWeight.isNullOrEmpty()) {
                    initCurrentWeightContainer.error = "Current Weight Required"
                } else if (goalsWeight.isNullOrEmpty()) {
                    initGoalWeightContainer.error = "Goal's Weight Required"
                } else if (calories.isNullOrEmpty()) {
                    initCaloryContainer.error = "Daily Calory Goals Required"
                } else if (date.isNullOrEmpty()) {
                    initDatePickerContainer.error = "Goal's Date Required"
                } else {
                    LogRegScreen.auth.createUserWithEmailAndPassword(
                        data?.getString("EMAIL").toString(),
                        data?.getString("PASSWORD").toString()
                    )
                        .addOnCompleteListener(this@InitializeScreen) { authTask ->
                            if (authTask.isSuccessful) {
                                // Authentication was successful, get the authenticated user
                                val currentUser = LogRegScreen.auth.currentUser

                                // Check if the user is not null
                                currentUser?.let {
                                    val user = Users(
                                        username = username,
                                        email = data?.getString("EMAIL").toString(),
                                        current_height = currHeight.toString().toInt(),
                                        current_weight = currWeight.toString().toInt(),
                                        goal_weight = goalsWeight.toString().toInt(),
                                        goal_calories = calories.toString().toInt(),
                                        goal_date = selectedDate,
                                        isAdmin = false
                                    )


                                    usersCollectionReference.document(currentUser.uid).set(user)
                                        .addOnCompleteListener { firestoreTask ->
                                            if (firestoreTask.isSuccessful) {
                                                Toast.makeText(
                                                    this@InitializeScreen,
                                                    "Data added successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                val intent = Intent(
                                                    this@InitializeScreen,
                                                    MainAppActivity::class.java
                                                )
                                                startActivity(intent)
                                            } else {
                                                Toast.makeText(
                                                    this@InitializeScreen,
                                                    "Error adding data. Please try again",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                } ?: run {
                                    // currentUser is null
                                    Toast.makeText(
                                        this@InitializeScreen,
                                        "Error getting authenticated user",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // Authentication failed
                                Toast.makeText(
                                    this@InitializeScreen,
                                    "Error creating user. Please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}
