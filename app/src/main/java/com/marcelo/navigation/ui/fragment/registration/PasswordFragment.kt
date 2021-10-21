package com.marcelo.navigation.ui.fragment.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.marcelo.navigation.R
import com.marcelo.navigation.extensions.dismissError
import com.marcelo.navigation.ui.fragment.viewmodels.LoginViewModel
import com.marcelo.navigation.ui.fragment.viewmodels.RegistrationViewModel
import kotlinx.android.synthetic.main.fragment_password.*

class PasswordFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    private val args: PasswordFragmentArgs by navArgs()

    private val navigation: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setTextCredencials()
        val invalidFields = showErrors()
        listenToRegistrationStateEvent(invalidFields)
        clickButtons()
    }

    private fun setTextCredencials() {
        textChooseCredentialsName.text = getString(R.string.choose_credentials_text_name, args.name)
    }

    private fun listenToRegistrationStateEvent(validationFields: Map<String, TextInputLayout>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner,{ registrationState ->
            when (registrationState) {
                is RegistrationViewModel.RegistrationState.RegistrationCompleted -> {
                    val token = registrationViewModel.authToken
                    val username = inputChooseCredentialsUsername.text.toString()

                    loginViewModel.authenticateToken(token, username)
                    navigation.popBackStack(R.id.profileFragment, false)
                }
                is RegistrationViewModel.RegistrationState.InvalidCredentials -> {
                    registrationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
            }
        })
    }

    private fun clickButtons() {
        buttonChooseCredentialsNext.setOnClickListener {
            val user = inputChooseCredentialsUsername.text.toString()
            val password = inputChooseCredentialsPassword.text.toString()

            registrationViewModel.createCredentials(user, password)
        }

       inputChooseCredentialsUsername.addTextChangedListener {
           inputLayoutChooseCredentialsUsername.dismissError()
       }

        inputChooseCredentialsPassword.addTextChangedListener {
            inputLayoutChooseCredentialsPassword.dismissError()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelRegistration()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelRegistration()
        return true
    }

    private fun cancelRegistration() {
        registrationViewModel.userCancelledRegistration()
        navigation.popBackStack(R.id.loginFragment, false)
    }

    private fun showErrors() = mapOf(
        RegistrationViewModel.INPUT_USERNAME.first to inputLayoutChooseCredentialsUsername,
        RegistrationViewModel.INPUT_PASSWORD.first to inputLayoutChooseCredentialsPassword
    )
}