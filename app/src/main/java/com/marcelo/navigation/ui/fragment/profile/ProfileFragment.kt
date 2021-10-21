package com.marcelo.navigation.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.marcelo.navigation.R
import com.marcelo.navigation.extensions.navigateWithAnimations
import com.marcelo.navigation.ui.fragment.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View?
    {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkStatusLogin()
    }

    private fun checkStatusLogin() {
        loginViewModel.authenticationStateEvent.observe(viewLifecycleOwner, {authState->
            when (authState) {
                is LoginViewModel.AuthenticationState.Authenticated -> {
                    txtNameProfile.text = getString(R.string.text_welcome_profile, loginViewModel.username)
                }
                is LoginViewModel.AuthenticationState.Unauthenticated -> {
                    findNavController().navigateWithAnimations(R.id.loginFragment)
                }
            }
        })
    }
}