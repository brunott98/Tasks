package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.tasks.databinding.FragmentAllTasksBinding
import com.devmasterteam.tasks.service.constants.TaskConstants.BUNDLE.TASKFILTER
import com.devmasterteam.tasks.service.constants.TaskConstants.BUNDLE.TASKID
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.view.adapter.TaskAdapter
import com.devmasterteam.tasks.viewmodel.TaskListViewModel

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val adapter = TaskAdapter()
    private var taskFilter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        val recycler = binding.recyclerAllTasks
        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)

        binding.recyclerAllTasks.adapter = adapter

        taskFilter = requireArguments().getInt(TASKFILTER,0)

        val listener = object: TaskListener{
            override fun onListClick(id: Int) {

                val intent =  Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TASKID,id)
                intent.putExtras(bundle)
                startActivity(intent)


            }

            override fun onDeleteClick(id: Int) {
                viewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                viewModel.status(id, isComplete = true)
            }

            override fun onUndoClick(id: Int) {
                viewModel.status(id, isComplete = false)
            }

        }

        adapter.attachListener(listener)





        // Cria os observadores
        observe()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.listAllTasks(taskFilter)
    }

    private fun observe() {

        viewModel.tasks.observe(viewLifecycleOwner){

            adapter.updateTask(it)

        }

        viewModel.delete.observe(viewLifecycleOwner){
            if(!it.showStatus()){
                Toast.makeText(context,it.showMessage(),Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.status.observe(viewLifecycleOwner){
            if(!it.showStatus()){
                Toast.makeText(context,it.showMessage(),Toast.LENGTH_SHORT).show()
            }
        }

    }
}