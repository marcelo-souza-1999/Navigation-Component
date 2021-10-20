package com.marcelo.roomcoroutines.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.marcelo.roomcoroutines.R
import com.marcelo.roomcoroutines.extensions.navigateWithAnimations
import com.marcelo.roomcoroutines.ui.fragment.login.LoginViewModel
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
        loginViewModel.authenticationState.observe(viewLifecycleOwner, {authState->
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