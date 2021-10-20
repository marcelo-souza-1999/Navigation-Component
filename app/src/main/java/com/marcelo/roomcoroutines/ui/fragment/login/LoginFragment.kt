package com.marcelo.roomcoroutines.ui.fragment.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import com.marcelo.roomcoroutines.R
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View?
    {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.authenticationState.observe(viewLifecycleOwner, { authState ->
            when(authState) {
                is LoginViewModel.AuthenticationState.InvalidAuth -> {
                    val validField: Map<String, TextInputLayout> = showErrors()
                    authState.fields.forEach {fieldInvalid ->
                        validField[fieldInvalid.first]?.error = getString(fieldInvalid.second)
                    }
                }
            }
        })

        btnLogin.setOnClickListener {
            val username = inputLoginUsername.text.toString()
            val password = inputLoginPassword.text.toString()

            viewModel.authentication(username, password)
        }
    }

    private fun showErrors() = mapOf (
        LoginViewModel.INPUT_USER_NAME.first to inputLayoutLoginUsername,
        LoginViewModel.INPUT_USER_PASS.first to inputLayoutLoginPassword)
}