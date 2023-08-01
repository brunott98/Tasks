package com.devmasterteam.tasks.service.model

class ValidationModel(val message: String = "") {


    private var status: Boolean = true
    private var validationMessage: String = ""

    init{
        if(message != ""){
            validationMessage = message
            status = false
        }
    }

    fun showStatus() = status
    fun showMessage() = validationMessage

}