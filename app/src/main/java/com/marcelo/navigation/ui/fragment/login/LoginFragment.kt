package com.marcelo.navigation.ui.fragment.login

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
import com.marcelo.navigation.R
import com.marcelo.navigation.extensions.dismissError
import com.marcelo.navigation.extensions.navigateWithAnimations
import com.marcelo.navigation.ui.fragment.viewmodels.LoginViewModel
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

        val validateFields = showErrors()
        listenToAuthenticationStateEvent(validateFields)
        clickButtons()
    }

    private fun clickButtons() {
        btnLogin.setOnClickListener {
            val username = inputLoginUsername.text.toString()
            val password = inputLoginPassword.text.toString()

            viewModel.authentication(username, password)
        }

        btnSignUp.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.show_to_registrationNavigation)
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

    private fun listenToAuthenticationStateEvent(validField: Map<String, TextInputLayout>) {
        viewModel.authenticationStateEvent.observe(viewLifecycleOwner, { authState ->
            when (authState) {
                is LoginViewModel.AuthenticationState.Authenticated -> {
                    findNavController().popBackStack()
                }
                is LoginViewModel.AuthenticationState.InvalidAuth -> {
                    authState.fields.forEach { fieldInvalid ->
                        validField[fieldInvalid.first]?.error = getString(fieldInvalid.second)
                    }
                }
            }
        })
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