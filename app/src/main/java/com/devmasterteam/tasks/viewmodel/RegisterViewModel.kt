package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _userNotification = MutableLiveData<ValidationModel>()
     val userNotification: LiveData<ValidationModel> = _userNotification

    fun create(name: String, email: String, password: String) {

        personRepository.create(name,email,password, object: APIListener<PersonModel>{
            override fun onSuccess(responseModel: PersonModel) {

                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, responseModel.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, responseModel.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, responseModel.name)

                RetrofitClient.addHeaders(responseModel.token, responseModel.personKey)
                _userNotification.value =  ValidationModel()

            }

            override fun onFailure(message: String) {
                _userNotification.value = ValidationModel(message)
            }
        })

    }

}