package com.marcelo.roomcoroutines.ui.fragment.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcelo.roomcoroutines.R

class LoginViewModel : ViewModel() {

    sealed class AuthenticationState {
        object Unauthenticated: AuthenticationState()
        object Authenticated: AuthenticationState()
        class InvalidAuth(val fields: List<Pair<String,Int>>) : AuthenticationState()
    }

    val authenticationState = MutableLiveData<AuthenticationState>()

    var username: String = ""

    init {
        refuseAuthentication()
    }

    fun refuseAuthentication () {
        authenticationState.value = AuthenticationState.Unauthenticated
    }

    fun authentication(username: String, passowrd: String) {
        if (isValidForm(username, passowrd)) {
            this.username = username
            authenticationState.value = AuthenticationState.Authenticated
        }
    }

    private fun isValidForm(username: String, passowrd: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()

        if (username.isEmpty()) {
            invalidFields.add(INPUT_USER_NAME)
        }

        if (passowrd.isEmpty()) {
            invalidFields.add(INPUT_USER_PASS)
        }

        if (invalidFields.isNotEmpty()) {
            authenticationState.value = AuthenticationState.InvalidAuth(invalidFields)
            return false
        }

        return true
    }

    companion object {
        val INPUT_USER_NAME = "INPUT_USER_NAME" to R.string.login_input_layout_error_invalid_username
        val INPUT_USER_PASS = "INPUT_USER_PASS" to R.string.login_input_layout_error_invalid_password
    }
}