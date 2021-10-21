package com.marcelo.roomcoroutines.ui.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.marcelo.roomcoroutines.R
import com.marcelo.roomcoroutines.extensions.dismissError
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.authenticationState.observe(viewLifecycleOwner, { authState ->
            when (authState) {
                is LoginViewModel.AuthenticationState.Authenticated -> {
                    findNavController().popBackStack()
                }
                is LoginViewModel.AuthenticationState.InvalidAuth -> {
                    val validField: Map<String, TextInputLayout> = showErrors()
                    authState.fields.forEach { fieldInvalid ->
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

        inputLoginUsername.addTextChangedListener {
            inputLayoutLoginUsername.dismissError()
        }

        inputLoginPassword.addTextChangedListener {
            inputLayoutLoginPassword.dismissError()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closeLogin()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        closeLogin()
        return true
    }

    private fun closeLogin() {
        viewModel.refuseAuthentication()
        findNavController().popBackStack(R.id.startFragment, false)
    }

    private fun showErrors() = mapOf(
        LoginViewModel.INPUT_USER_NAME.first to inputLayoutLoginUsername,
        LoginViewModel.INPUT_USER_PASS.first to inputLayoutLoginPassword)
}