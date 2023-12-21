package com.example.calorify20.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorify20.R
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.dataClass.Preset
import com.example.calorify20.databinding.ActivityAdminPresetScreenBinding
import com.example.calorify20.mainApp.AddActivityScreen
import com.example.calorify20.recyclerView.PresetAdminRvAdapter
import com.example.calorify20.recyclerView.PresetRvAdapter

class AdminPresetScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPresetScreenBinding
    private val prefListLiveData: MutableLiveData<List<Preset>> by lazy {
        MutableLiveData<List<Preset>>()
    }
    private val presetCollectionRef = LogRegScreen.firestore.collection("preset")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPresetScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding) {
            adminListRv.layoutManager = LinearLayoutManager(this@AdminPresetScreen)
            adminAddButton.setOnClickListener {
                val intent = Intent(this@AdminPresetScreen, AdminAddPresetScreen::class.java)
                startActivity(intent)
            }

            adminSignOutButton.setOnClickListener {
                LogRegScreen.auth.signOut()
                val intent = Intent(this@AdminPresetScreen, LogRegScreen::class.java).apply {
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
            }

            adminSearch.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    getAllPreset()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })


        }
        observePreset()
        getAllPreset()
    }

    private fun observePreset() {
        prefListLiveData.observe(this@AdminPresetScreen){
                pres ->
            var preset = pres.toMutableList()
            binding.adminListRv.adapter = PresetAdminRvAdapter(preset, {
                val bundle = Bundle()
                bundle.putString("ID", it.id.toString())
                bundle.putString("TYPE", it.type.toString())
                bundle.putString("OCCASION", it.occasion.toString())
                bundle.putString("ITEM", it.item.toString())
                val intent = Intent(this@AdminPresetScreen, AdminEditPresetScreen::class.java).apply { putExtras(bundle) }
                startActivity(intent)
            }, {
                presetCollectionRef.document(it.id).delete()
            })
        }
    }

    private fun getAllPreset() {
        val query = if (binding.adminSearch.text.toString().isEmpty()) {
            presetCollectionRef
        } else {
            presetCollectionRef.whereEqualTo(
                "item",
                binding.adminSearch.text.toString()
            )
        }
        query.addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this, "Error listening to preset data", Toast.LENGTH_SHORT)
                    .show()
            }
            val preset = arrayListOf<Preset>()
            value?.forEach { documentSnapshot ->
                preset.add(
                    Preset(
                        documentSnapshot.id,
                        documentSnapshot.get("type").toString(),
                        documentSnapshot.get("occasion").toString(),
                        documentSnapshot.get("item").toString()
                    )
                )
            }
            if (preset != null) {
                prefListLiveData.postValue(preset)
            }
        }
    }
}