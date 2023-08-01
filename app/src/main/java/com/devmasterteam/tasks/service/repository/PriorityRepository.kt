package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) : BaseRepository(context) {

    private val remotePriorityService = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDAO()

    companion object{

        private val cache = mutableMapOf<Int,String>()
        fun getDescription(id: Int): String{
            return cache[id]?: ""
        }

        fun setDescription(id: Int, descriptionStr: String){
            cache[id] = descriptionStr
        }

    }


    fun getDescription(id: Int ): String{

        val cached = PriorityRepository.getDescription(id)

        return if(cached==""){

            val description = database.getDescription(id)
            PriorityRepository.setDescription(id,description)
            description

        } else {
            cached
        }

    }

    fun list(listener: APIListener<List<PriorityModel>>) {

        val call = remotePriorityService.list()
        executeCall(call,listener)

    }

    fun save(list: List<PriorityModel>) {
        database.clear()
        database.save(list)
    }



    fun list(): List<PriorityModel> {
        return database.list()
    }


}