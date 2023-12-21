package com.example.calorify20.mainApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.dataClass.Preset
import com.example.calorify20.databinding.ActivityChoicesBinding
import com.example.calorify20.recyclerView.PresetRvAdapter
import com.google.firebase.firestore.CollectionReference

class ChoicesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChoicesBinding
    private lateinit var presetCollectionRef: CollectionReference
    private val presListLiveData: MutableLiveData<List<Preset>> by lazy {
        MutableLiveData<List<Preset>>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presetCollectionRef = LogRegScreen.firestore.collection("preset")
        with(binding){
            choicesListRv.layoutManager = LinearLayoutManager(this@ChoicesActivity)
            choicesAddButton.setOnClickListener {
                val intent = Intent(this@ChoicesActivity, AddActivityScreen::class.java)
                startActivity(intent)
            }
        }

        presListLiveData.observe(this){
            pref ->
            var listPreference = pref.toMutableList()
            binding.choicesListRv.adapter = PresetRvAdapter(listPreference){
                preset ->
                val bundle = Bundle()
                bundle.putString("TYPE", preset.type)
                bundle.putString("OCCASION", preset.occasion)
                bundle.putString("ITEM", preset.item)
                val intent = Intent(this@ChoicesActivity, AddActivityScreen::class.java).apply { putExtras(bundle) }
                startActivity(intent)
            }
        }

        binding.choicesSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getAllPreset()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        getAllPreset()
    }



    private fun getAllPreset(){
        val query = if (binding.choicesSearch.text.toString().isEmpty()) {
            presetCollectionRef
        } else {
            presetCollectionRef.whereEqualTo("item", binding.choicesSearch.text.toString())
        }
        query.addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(this, "Error listening to preset data", Toast.LENGTH_SHORT).show()
            }
            val preset = arrayListOf<Preset>()
            value?.forEach{
                    documentSnapshot ->
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
                presListLiveData.postValue(preset)
            }
        }
    }
}
