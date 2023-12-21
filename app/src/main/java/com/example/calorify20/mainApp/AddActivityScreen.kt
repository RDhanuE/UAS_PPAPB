package com.example.calorify20.mainApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.calorify20.R
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.databinding.ActivityAddScreenBinding
import com.example.calorify20.room.CalorifyDatabase
import com.example.calorify20.room.InOutTake
import com.example.calorify20.room.InOutTakeDao
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddActivityScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAddScreenBinding
    private lateinit var mInOutTakeDao: InOutTakeDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val roomDb = CalorifyDatabase.getDatabase(this)
        mInOutTakeDao = roomDb!!.InOutTakeDao()!!
        val data = intent.extras
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setTitleText("Select Time")
            .setInputMode(INPUT_MODE_KEYBOARD)
            .build()

        with(binding){
            val occasion = addOccasionContainer.editText as? MaterialAutoCompleteTextView
            addType.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString() == "Intake") {
                        addOccasionContainer.isEnabled = true
                        addTimePickerContainer.isEnabled = true
                        addItemContainer.isEnabled = true
                        addCaloryContainer.isEnabled = true
                        addItemContainer.hint = "Food / Drink"
                        occasion?.setSimpleItems(resources.getStringArray(R.array.Occassion_Intake))
                    } else if (p0.toString() == "Outtake") {
                        addOccasionContainer.isEnabled = true
                        addTimePickerContainer.isEnabled = true
                        addItemContainer.isEnabled = true
                        addCaloryContainer.isEnabled = true
                        addItemContainer.hint = "Activity"
                        occasion?.setSimpleItems(resources.getStringArray(R.array.Occassion_Outtake))
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            if (data != null) {
                addOccasionContainer.isEnabled = true
                addTimePickerContainer.isEnabled = true
                addItemContainer.isEnabled = true
                addCaloryContainer.isEnabled = true
                addType.setText(data.getString("TYPE").toString(), false)
                addOccasion.setText(data.getString("OCCASION").toString(), false)
                addItem.setText(data.getString("ITEM").toString())
            }
            addTimePicker.setOnClickListener{
                picker.addOnPositiveButtonClickListener {
                    val hour = picker.hour
                    val minute = picker.minute
                    val formated = formatTime(hour, minute)
                    addTimePicker.setText(formated)
                }
                picker.show(supportFragmentManager, "TimePicker")
            }

            addButton.setOnClickListener {
                val type = addType.text.toString()
                val occasion = addOccasion.text.toString()
                val time = addTimePicker.text.toString()
                val item = addItem.text.toString()
                val calory = addCalory.text.toString()

                if (type.isNullOrEmpty()){
                    addTypeContainer.error = "Activity type required"
                } else if (occasion.isNullOrEmpty()) {
                    addOccasionContainer.error = "Occasion required"
                } else if (time.isNullOrEmpty()) {
                    addTimePickerContainer.error = "Time required"
                } else if (item.isNullOrEmpty()) {
                    addItemContainer.error = "Food / beverages / actvity required"
                } else if (calory.isNullOrEmpty()) {
                    addCaloryContainer.error = "Total Calories required"
                } else {
                    insert(
                        InOutTake(
                            type = type,
                            occasion = occasion,
                            time = time,
                            item = item,
                            calory = calory.toInt(),
                            uid = LogRegScreen.auth.currentUser?.uid.toString()
                        )
                    )

                    val intent = Intent(this@AddActivityScreen, MainAppActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    private fun insert(inOutTake: InOutTake) {
        executorService.execute{
            mInOutTakeDao.insertActivity(inOutTake)
        }
    }

}