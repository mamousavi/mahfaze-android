package com.miramir.mahfaze.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.miramir.mahfaze.data.model.Resource
import com.miramir.mahfaze.data.model.User
import com.miramir.mahfaze.data.remote.UserService
import com.miramir.mahfaze.util.enqueueAndUnwrap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userService: UserService) {
    fun loginOrRegister(email: String, password: String): LiveData<Resource<User>> {
        val result = MutableLiveData<Resource<User>>()
        result.value = Resource.loading(null)
        userService.loginOrRegister(email, password).enqueueAndUnwrap({
            result.value = Resource.success(it)
        }, {
            result.value = Resource.error(it, null)
        })
        return result
    }
}