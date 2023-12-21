package com.example.calorify20.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.calorify20.R
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.dataClass.Preset
import com.example.calorify20.databinding.ActivityAdminEditPresetScreenBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class AdminEditPresetScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAdminEditPresetScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditPresetScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        val presetCollection = LogRegScreen.firestore.collection("preset")

        with(binding){
            val occasion = adminEditOccasionContainer.editText as? MaterialAutoCompleteTextView
            adminEditOccasionContainer.isEnabled = true
            adminEditItemContainer.isEnabled = true
            adminEditType.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString() == "Intake") {
                        adminEditButton.hint = "Food / Drink"
                        occasion?.setSimpleItems(resources.getStringArray(R.array.Occassion_Intake))
                    } else if (p0.toString() == "Outtake") {
                        adminEditItemContainer.hint = "Activity"
                        occasion?.setSimpleItems(resources.getStringArray(R.array.Occassion_Outtake))
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })

            adminEditOccasion.setText(data?.getString("OCCASION", ""))
            adminEditItem.setText(data?.getString("ITEM", ""))
            adminEditButton.setOnClickListener {
                adminEditTypeContainer.error = null
                adminEditOccasionContainer.error = null
                adminEditItemContainer.error = null

                if (adminEditType.text.isNullOrEmpty()){
                    adminEditTypeContainer.error = "Please choose the type of the activity"
                } else if (adminEditOccasion.text.isNullOrEmpty()){
                    adminEditOccasionContainer.error = "Please choose the occason of the activity"
                } else if (adminEditItem.text.isNullOrEmpty()){
                    if (adminEditType.text.toString() == "Intake"){
                        adminEditItemContainer.error = "Item required"
                    } else if (adminEditType.text.toString() == "Outtake"){
                        adminEditTypeContainer.error = "Activity required"
                    }
                } else {
                    val preset = Preset(
                        type = adminEditType.text.toString(),
                        occasion = adminEditOccasion.text.toString(),
                        item = adminEditItem.text.toString()
                    )
                    presetCollection.document(data?.getString("ID").toString()).set(preset).addOnSuccessListener {
                        finish()
                    }.addOnFailureListener{
                        Toast.makeText(this@AdminEditPresetScreen, "Edit data failed" + it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}