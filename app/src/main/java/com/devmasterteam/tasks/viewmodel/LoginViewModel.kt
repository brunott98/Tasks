package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.system.BiometricHelper

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application)
    private val priorityRepository = PriorityRepository(application)

    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login:LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser:LiveData<Boolean> = _loggedUser

    fun doLogin(email: String, password: String) {

        personRepository.login(email, password, object : APIListener<PersonModel> {

            override fun onSuccess(responseModel: PersonModel) {

                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, responseModel.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, responseModel.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, responseModel.name)

                RetrofitClient.addHeaders(responseModel.token, responseModel.personKey)

                _login.value = ValidationModel()

            }

            override fun onFailure(message: String) {

                _login.value = ValidationModel(message)

            }

        })

    }

    fun verifyAuthentication() {

        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeaders(token, person)

        val logged = (token!= "" && person!= "")


        if(!logged){

            priorityRepository.list(object: APIListener<List<PriorityModel>>{
                override fun onSuccess(responseModel: List<PriorityModel>) {
                    priorityRepository.save(responseModel)
                }

                override fun onFailure(message: String) {
                    val s = ""
                }
            })

        }

        _loggedUser.value = (logged && BiometricHelper.isBiometricAvaible(getApplication()))

    }

}