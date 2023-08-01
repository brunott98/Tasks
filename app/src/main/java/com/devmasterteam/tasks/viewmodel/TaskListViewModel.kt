package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var taskFilter = 0

    private val _tasks= MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    fun listAllTasks(filter: Int){
        taskFilter = filter

        val listener = object: APIListener<List<TaskModel>>{
            override fun onSuccess(responseModel: List<TaskModel>) {

                responseModel.forEach{
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }
                _tasks.value = responseModel

            }

            override fun onFailure(message: String) {
                TODO("Not yet implemented")
            }

        }

        when (filter) {
            TaskConstants.FILTER.ALL -> {
                taskRepository.listAllTasks(listener)
            }
            TaskConstants.FILTER.NEXT -> {
                taskRepository.listNextSevenDays(listener)
            }
            else -> {
                taskRepository.listOverdue(listener)
            }
        }
    }

    fun delete(id: Int){
        taskRepository.delete(id, object : APIListener<Boolean>{
            override fun onSuccess(responseModel: Boolean) {
                listAllTasks(taskFilter)
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }

        })
    }

    fun status(id: Int, isComplete: Boolean ){

        val listener = object : APIListener<Boolean>{
            override fun onSuccess(responseModel: Boolean) {
                listAllTasks(taskFilter)
            }

            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }

        }

        if(isComplete) {
            taskRepository.complete(id,listener)

        } else{
            taskRepository.undo(id,listener)
        }

    }

}