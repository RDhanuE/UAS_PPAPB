package com.example.calorify20.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calorify20.admin.AdminPresetScreen
import com.example.calorify20.mainApp.MainAppActivity
import com.example.calorify20.mainApp.PrefManager
import com.example.calorify20.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        prefManager = PrefManager.getInstance(this@LoginFragment.requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            loginButtonToReg.setOnClickListener{
                (requireActivity() as? LogRegScreen)?.switchFragment(0)
            }
            loginButton.setOnClickListener{
                loginEmailContainer.error = null
                loginPasswordContainer.error = null
                var email = loginEmail.text.toString()
                var password = loginPassword.text.toString()
                if (email.isNullOrEmpty()) {
                    loginEmailContainer.error = "Email required"
                } else if (password.isNullOrEmpty()) {
                    loginPasswordContainer.error = "Password Required"
                } else {
                    if (loginCheckbox.isChecked){
                        prefManager.saveEmail(email)
                        prefManager.savePassword(password)
                        prefManager.setLoggedIn(true)
                    }
                    LogRegScreen.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            LogRegScreen.firestore.collection("users").document(LogRegScreen.auth.currentUser?.uid.toString()).get().addOnSuccessListener {
                                document ->
                                if(document.exists()){
                                    if(document.getBoolean("isAdmin") == true){
                                        val intent = Intent(this@LoginFragment.requireActivity(), AdminPresetScreen::class.java)
                                        startActivity(intent)
                                    }
                                    else{
                                        val intent = Intent(this@LoginFragment.requireActivity(), MainAppActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this@LoginFragment.requireActivity(), "Login Failed. Please Try Again", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}