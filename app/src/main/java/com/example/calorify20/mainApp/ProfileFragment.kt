package com.example.calorify20.mainApp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefManager = PrefManager.getInstance(this@ProfileFragment.requireActivity())
        var current_user = LogRegScreen.auth.currentUser!!
        var current_user_data = LogRegScreen.firestore.collection("users").document(current_user.uid)
        with(binding) {
            current_user_data.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val username = documentSnapshot.getString("username")
                    val height = documentSnapshot.getLong("current_height")
                    val weight = documentSnapshot.getLong("current_weight")
                    val goal_weight = documentSnapshot.getLong("goal_weight")
                    val calory = documentSnapshot.getLong("goal_calories")
                    val date = documentSnapshot.getString("goal_date")
                    profileUsername.setText(username)
                    profileDataHeight2.setText(height.toString())
                    profileDataWeight2.setText(weight.toString())
                    profileDataGoalWeight2.setText(goal_weight.toString())
                    profileDataCalory2.setText(calory.toString())
                    profileDataGoalDate2.setText(date)
                }
            }


            profileEditButton.setOnClickListener{
                val intent = Intent(this@ProfileFragment.requireActivity(), EditProfileActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            profileSignOutButton.setOnClickListener {
                LogRegScreen.auth.signOut()
                prefManager.clear()
                val intent = Intent(this@ProfileFragment.requireActivity(), LogRegScreen::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }
}