package com.miramir.mahfaze.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.miramir.mahfaze.data.model.Resource
import com.miramir.mahfaze.data.model.User
import com.miramir.mahfaze.data.repository.UserRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {
    class Credentials(val email: String, val password: String)

    private val credentials = MutableLiveData<Credentials>()

    val result: LiveData<Resource<User>> = Transformations.switchMap(credentials) {
        userRepository.loginOrRegister(it.email, it.password)
    }

    fun login(email: String, password: String) {
        credentials.value = Credentials(email, password)
    }

    fun getCredentials() = credentials.value
}