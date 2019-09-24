package com.android.itrip.viewModels

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.itrip.util.ObservableViewModel


class LoginViewModel : ObservableViewModel() {


    val email = ObservableField<String>("")
    private val _email = ObservableField<String>(email.get())

    fun emailChanged(newValue: CharSequence) {
        try {
            email.set((newValue.toString()))
        } catch (e: NumberFormatException) {
            return
        }
        _email.set(email.get())
        enableLogin()
    }

    val password = ObservableField<String>("")
    private val _password = ObservableField<String>(password.get())

    fun passwordChanged(newValue: CharSequence) {
        try {
            password.set((newValue.toString()))
        } catch (e: NumberFormatException) {
            return
        }
        _password.set(password.get())
        enableLogin()
    }

    private val _enableLogin = MutableLiveData<Boolean>()
    val enableLogin: LiveData<Boolean>
        get() = _enableLogin

    private fun enableLogin() :Boolean{
        _enableLogin.value = !email.get().isNullOrBlank() && !password.get().isNullOrBlank()
        return _enableLogin.value!!
    }

    fun loginCorrect(): Boolean {
        Log.i("LoginViewModel", "Email: " + _email.get())
        Log.i("LoginViewModel", "Password: " + _password.get())
        return (_email.get() == "admin" && _password.get() == "123")
    }

}