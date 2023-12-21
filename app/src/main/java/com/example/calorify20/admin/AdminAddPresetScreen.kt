package com.example.calorify20.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.calorify20.R
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.dataClass.Preset
import com.example.calorify20.databinding.ActivityAdminAddPresetScreenBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class AdminAddPresetScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddPresetScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddPresetScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val presetCollection = LogRegScreen.firestore.collection("preset")

        with(binding){
            val occasion = adminAddOccasionContainer.editText as? MaterialAutoCompleteTextView
            adminAddType.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString() == "Intake") {
                        adminAddOccasionContainer.isEnabled = true
                        adminAddItemContainer.isEnabled = true
                        adminAddItemContainer.hint = "Food / Drink"
                        occasion?.setSimpleItems(resources.getStringArray(R.array.Occassion_Intake))
                    } else if (p0.toString() == "Outtake") {
                        adminAddOccasionContainer.isEnabled = true
                        adminAddItemContainer.isEnabled = true
                        adminAddItemContainer.hint = "Activity"
                        occasion?.setSimpleItems(resources.getStringArray(R.array.Occassion_Outtake))
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })

            adminAddButton.setOnClickListener {
                adminAddTypeContainer.error = null
                adminAddOccasionContainer.error = null
                adminAddItemContainer.error = null
                if (adminAddType.text.isNullOrEmpty()){
                    adminAddTypeContainer.error = "Please choose the type of the activity"
                } else if (adminAddOccasion.text.isNullOrEmpty()){
                    adminAddOccasionContainer.error = "Please choose the occason of the activity"
                } else if (adminAddItem.text.isNullOrEmpty()){
                    if (adminAddType.text.toString() == "Intake"){
                        adminAddItemContainer.error = "Item required"
                    } else if (adminAddType.text.toString() == "Outtake"){
                        adminAddTypeContainer.error = "Activity required"
                    }
                } else {
                    val preset = Preset(
                        type = adminAddType.text.toString(),
                        occasion = adminAddOccasion.text.toString(),
                        item = adminAddItem.text.toString()
                    )
                    presetCollection.add(preset).addOnSuccessListener {
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this@AdminAddPresetScreen, "Adding data failed" + it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}