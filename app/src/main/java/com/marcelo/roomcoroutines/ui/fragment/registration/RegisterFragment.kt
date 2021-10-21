package com.marcelo.roomcoroutines.ui.fragment.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.marcelo.roomcoroutines.R
import com.marcelo.roomcoroutines.extensions.dismissError
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val validateFields = showErrors()
        listenerRegistrationViewModelEvents(validateFields)
        clickButtons()
    }

    private fun clickButtons() {
        buttonProfileDataNext.setOnClickListener {
            val name = inputProfileDataName.text.toString()
            val bio = inputProfileDataBio.text.toString()
            registrationViewModel.collectProfileData(name, bio)
        }

        inputProfileDataName.addTextChangedListener {
            inputLayoutProfileDataName.dismissError()
        }

        inputProfileDataBio.addTextChangedListener {
            inputLayoutProfileDataBio.dismissError()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closeRegister()
        }
    }

    private fun closeRegister() {
        registrationViewModel.userCancelledRegistration()
        findNavController().popBackStack(R.id.loginFragment, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        closeRegister()
        return true
    }

    private fun listenerRegistrationViewModelEvents(validateFields: Map<String, TextInputLayout>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, { registerState ->
            when (registerState) {
                is RegistrationViewModel.RegistrationState.CollectCredentials -> {
                    val name = inputProfileDataName.text.toString()
                    val directions = RegisterFragmentDirections.showToPasswordFragment(name)

                    findNavController().navigate(directions)
                }
                is RegistrationViewModel.RegistrationState.InvalidProfileData -> {
                    registerState.fields.forEach { fieldInvalid ->
                        validateFields[fieldInvalid.first]?.error = getString(fieldInvalid.second)
                    }
                }
            }
        })
    }

    private fun showErrors() = mapOf(
        RegistrationViewModel.INPUT_NAME.first to inputLayoutProfileDataName,
        RegistrationViewModel.INPUT_BIO.first to inputLayoutProfileDataBio,
    )
}