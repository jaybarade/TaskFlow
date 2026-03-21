package com.example.taskflow.Model

data class TaskModel(
    val id: Int = 0,
    val taskName: String,
    val taskDesc: String,
    val taskDate: String,
    val empName: String,
    val status: Int
)
