package com.devmasterteam.tasks.service.listener

interface APIListener<ResponseModel> {

    fun onSuccess(responseModel: ResponseModel)
    fun onFailure(message: String)

}