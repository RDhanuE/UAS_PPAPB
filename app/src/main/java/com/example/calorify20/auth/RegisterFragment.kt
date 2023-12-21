package com.example.calorify20.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calorify20.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            registerButtonToLog.setOnClickListener{
                (requireActivity() as? LogRegScreen)?.switchFragment(1)
            }

            registerButton.setOnClickListener{
                registerUsernameContainer.error = null
                registerEmailContainer.error = null
                registerPasswordContainer.error = null
                registerPasswordConfirmContainer.error = null
                var username = registerUsername.text.toString()
                var email = registerEmail.text.toString()
                var password = registerPassword.text.toString()
                var confirmpassword = registerPasswordConfirm.text.toString()
                if (username.isNullOrEmpty()) {
                    registerUsernameContainer.error = "Username Required"
                } else  if (email.isNullOrEmpty()) {
                    registerEmailContainer.error = "Email required"
                } else if (password.isNullOrEmpty()) {
                    registerPasswordContainer.error = "Password required"
                } else if (confirmpassword.isNullOrEmpty()) {
                    registerPasswordConfirmContainer.error = "Confirm your password"
                } else if (password != confirmpassword) {
                    registerPasswordConfirmContainer.error = "Password doesnt match"
                } else {
                    var bundle = Bundle()
                    bundle.putString("USERNAME", username)
                    bundle.putString("EMAIL", email)
                    bundle.putString("PASSWORD", password)
                    val intent = Intent(this@RegisterFragment.requireActivity(), InitializeScreen::class.java).apply { putExtras(bundle) }
                    startActivity(intent)
                }
            }

        }
    }
}
