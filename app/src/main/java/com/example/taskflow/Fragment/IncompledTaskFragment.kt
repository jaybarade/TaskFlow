package com.example.taskflow.Fragment

import android.app.DatePickerDialog
import com.example.taskflow.TaskAdapter


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskflow.Model.TaskModel
import com.example.taskflow.TaskDB.SQLiteDBHelper
import com.example.taskflow.databinding.AddTaskBindingBinding
import com.example.taskflow.databinding.FragmentIncompledTaskBinding
import java.util.Calendar

class IncompledTaskFragment : Fragment() {

    private lateinit var binding: FragmentIncompledTaskBinding
    private lateinit var dbHelper: SQLiteDBHelper
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncompledTaskBinding.inflate(inflater, container, false)

        // ✅ Init DB
        dbHelper = SQLiteDBHelper(requireContext())

        // ✅ Load INCOMPLETE tasks (status = 0)
        val incompleteList = dbHelper.getTasksByStatus(0)

        // ✅ Setup Adapter
        adapter = TaskAdapter(
            incompleteList,
            onStatusChanged = { task, newStatus ->
                // Update status in DB
                dbHelper.updateTaskStatus(task.id, newStatus)
                refreshList()
            },
            onDeleteClicked = { task ->
                // Delete task
                dbHelper.deleteTask(task.id)
                refreshList()
            }
            ,
            onEditClicked = { task ->
                showUpdateTaskDialog(task)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        binding.swipeRefresh.setOnRefreshListener {
            refreshList()
            binding.swipeRefresh.isRefreshing = false
        }


        return binding.root
    }

    private fun refreshList() {
        val updatedList = dbHelper.getTasksByStatus(0)
        adapter.updateList(updatedList)
    }

    private fun showUpdateTaskDialog(task: TaskModel) {
        val dialogBinding = AddTaskBindingBinding.inflate(layoutInflater)


        dialogBinding.etTaskName.setText(task.taskName)
        dialogBinding.etTaskDesc.setText(task.taskDesc)
        dialogBinding.etDate.setText(task.taskDate)
        dialogBinding.etEmpName.setText(task.empName)

        dialogBinding.tvTitle.text="Update Task"

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    dialogBinding.etDate.setText("${d}/${m + 1}/${y}")
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        dialogBinding.btnSubmit.setText("Update")

        dialogBinding.btnSubmit.setOnClickListener {
            val name = dialogBinding.etTaskName.text.toString().trim()
            val desc = dialogBinding.etTaskDesc.text.toString().trim()
            val date = dialogBinding.etDate.text.toString().trim()
            val emp = dialogBinding.etEmpName.text.toString().trim()

            if (name.isEmpty() || desc.isEmpty() || date.isEmpty() || emp.isEmpty()) {
                Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.updateTask(task.id, name, desc, date, emp)

            if (success) {
                Toast.makeText(requireContext(), "Task updated", Toast.LENGTH_SHORT).show()
                refreshList()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
