package com.marcelo.roomcoroutines.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcelo.roomcoroutines.R

class LoginViewModel : ViewModel() {

    sealed class AuthenticationState {
        object Unauthenticated: AuthenticationState()
        object Authenticated: AuthenticationState()
        class InvalidAuth(val fields: List<Pair<String,Int>>) : AuthenticationState()
    }

    var username: String = ""
    var token: String = ""

    private val _authenticationStateEvent = MutableLiveData<AuthenticationState>()
    val authenticationStateEvent: LiveData<AuthenticationState>
    get() = _authenticationStateEvent

    init {
        refuseAuthentication()
    }

    fun refuseAuthentication () {
        _authenticationStateEvent.value = AuthenticationState.Unauthenticated
    }

    fun authenticateToken(token: String, username: String) {
        this.token = token
        this.username = username
        _authenticationStateEvent.value = AuthenticationState.Authenticated
    }

    fun authentication(username: String, passowrd: String) {
        if (isValidForm(username, passowrd)) {
            this.username = username
            _authenticationStateEvent.value = AuthenticationState.Authenticated
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
            _authenticationStateEvent.value = AuthenticationState.InvalidAuth(invalidFields)
            return false
        }

        return true
    }

    companion object {
        val INPUT_USER_NAME = "INPUT_USER_NAME" to R.string.login_input_layout_error_invalid_username
        val INPUT_USER_PASS = "INPUT_USER_PASS" to R.string.login_input_layout_error_invalid_password
    }
}