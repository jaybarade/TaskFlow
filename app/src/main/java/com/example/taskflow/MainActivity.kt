package com.example.taskflow

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskflow.Fragment.CompletedTaskFragment
import com.example.taskflow.Fragment.IncompledTaskFragment
import com.example.taskflow.TaskDB.SQLiteDBHelper
import com.example.taskflow.databinding.ActivityMainBinding
import com.example.taskflow.databinding.AddTaskBindingBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }



        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, IncompledTaskFragment())
            .commit()

        selectIncomplete()

        binding.tvIncomplete.setOnClickListener {

            selectIncomplete()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, IncompledTaskFragment())
                .commit()
        }

        binding.tvCompleted.setOnClickListener {
            selectCompleted()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CompletedTaskFragment())
                .commit()
        }






    }






    private fun selectIncomplete() {
        binding.indicatorIncomplete.setBackgroundColor(Color.RED)
        binding.indicatorCompleted.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun selectCompleted() {
        binding.indicatorCompleted.setBackgroundColor(Color.GREEN)
        binding.indicatorIncomplete.setBackgroundColor(Color.TRANSPARENT)
    }


    private fun showAddTaskDialog() {
        val dialogBinding = AddTaskBindingBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()


        dialogBinding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, y, m, d ->
                    val selectedDate = "${d}/${m + 1}/${y}"
                    dialogBinding.etDate.setText(selectedDate)
                },
                year, month, day
            )
            datePicker.show()
        }


        dialogBinding.btnSubmit.setOnClickListener {
            val taskName = dialogBinding.etTaskName.text.toString().trim()
            val taskDesc = dialogBinding.etTaskDesc.text.toString().trim()
            val taskDate = dialogBinding.etDate.text.toString().trim()
            val empName = dialogBinding.etEmpName.text.toString().trim()


            when {
                taskName.isEmpty() -> {
                    dialogBinding.etTaskName.error = "Task name required"
                }
                taskDesc.isEmpty() -> {
                    dialogBinding.etTaskDesc.error = "Description required"
                }
                taskDate.isEmpty() -> {
                    dialogBinding.etDate.error = "Select date"
                }
                empName.isEmpty() -> {
                    dialogBinding.etEmpName.error = "Employee name required"
                }
                else -> {
                    AddApi(
                        taskName = taskName,
                        taskDescription = taskDesc,
                        date = taskDate,
                        employeeName = empName
                    )

                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                    currentFragment?.let {
                        supportFragmentManager.beginTransaction()
                            .detach(it)
                            .attach(it)
                            .commit()
                    }

                    Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                }
            }
        }

        dialog.show()
    }

    private fun AddApi(
        taskName: String,
        taskDescription: String,
        date: String,
        employeeName: String
    ) {
        Log.d("AddApi", "Task Name: $taskName")
        Log.d("AddApi", "Task Description: $taskDescription")
        Log.d("AddApi", "Date: $date")
        Log.d("AddApi", "Employee Name: $employeeName")

        val dbHelper = SQLiteDBHelper(this)

        dbHelper.insertTask(
            taskName =taskName ,
            taskDesc = taskDescription,
            taskDate = date,
            empName = employeeName
        )




    }

}