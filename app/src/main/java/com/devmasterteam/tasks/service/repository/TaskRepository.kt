package com.devmasterteam.tasks.service.repository


import android.content.Context
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context) : BaseRepository(context) {

    private val remoteTaskService = RetrofitClient.getService(TaskService::class.java)

    fun create(task: TaskModel, listener: APIListener<Boolean>) {

        val call = remoteTaskService.createTask(
            task.priorityId, task.description,
            task.dueDate, task.complete
        )
        executeCall(call,listener)

    }

    fun update(task: TaskModel, listener: APIListener<Boolean>) {

        val call = remoteTaskService.updateTask(
            task.id, task.priorityId, task.description,
            task.dueDate, task.complete
        )
        executeCall(call,listener)

    }

    fun listAllTasks(listener: APIListener<List<TaskModel>>){

        val call = remoteTaskService.allTasksList()

        executeCall(call,listener)
    }

    fun listNextSevenDays(listener: APIListener<List<TaskModel>>){

        val call = remoteTaskService.listNextTasks()
        executeCall(call,listener)
    }

    fun listOverdue(listener: APIListener<List<TaskModel>>){

        val call = remoteTaskService.listOverdueTasks()
        executeCall(call,listener)

    }

    fun delete(id:Int, listener: APIListener<Boolean>){
        val call = remoteTaskService.delete(id)
        executeCall(call,listener)

    }

    fun complete(id:Int, listener: APIListener<Boolean>){
        val call = remoteTaskService.completeTask(id)
        executeCall(call,listener)

    }

    fun undo(id:Int, listener: APIListener<Boolean>){
        val call = remoteTaskService.undoTask(id)
        executeCall(call,listener)

    }

    fun loadTask(id:Int, listener: APIListener<TaskModel>){
        val call = remoteTaskService.loadTask(id)
        executeCall(call,listener)

    }


}