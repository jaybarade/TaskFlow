package com.example.taskflow.TaskDB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taskflow.Model.TaskModel

class SQLiteDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TaskDB.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_TASK = "tasks"

        private const val COL_ID = "id"
        private const val COL_TASK_NAME = "task_name"
        private const val COL_TASK_DESC = "task_desc"
        private const val COL_TASK_DATE = "task_date"
        private const val COL_EMP_NAME = "emp_name"
        private const val COL_STATUS = "status" // 0 = Incomplete, 1 = Complete
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_TASK (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TASK_NAME TEXT NOT NULL,
                $COL_TASK_DESC TEXT NOT NULL,
                $COL_TASK_DATE TEXT NOT NULL,
                $COL_EMP_NAME TEXT NOT NULL,
                $COL_STATUS INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASK")
        onCreate(db)
    }


    fun insertTask(
        taskName: String,
        taskDesc: String,
        taskDate: String,
        empName: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TASK_NAME, taskName)
            put(COL_TASK_DESC, taskDesc)
            put(COL_TASK_DATE, taskDate)
            put(COL_EMP_NAME, empName)
            put(COL_STATUS, 0)
        }

        val result = db.insert(TABLE_TASK, null, values)
        db.close()
        return result != -1L
    }

//get task by status
    fun getTasksByStatus(status: Int): ArrayList<TaskModel> {
        val list = ArrayList<TaskModel>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_TASK WHERE $COL_STATUS = ? ORDER BY $COL_ID DESC",
            arrayOf(status.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val task = TaskModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    taskName = cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_NAME)),
                    taskDesc = cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_DESC)),
                    taskDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK_DATE)),
                    empName = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMP_NAME)),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STATUS))
                )
                list.add(task)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    // Update Task Status (0 = Incomplete, 1 = Complete)
    fun updateTaskStatus(taskId: Int, newStatus: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_STATUS, newStatus)
        }

        val result = db.update(
            TABLE_TASK,
            values,
            "$COL_ID = ?",
            arrayOf(taskId.toString())
        )
        db.close()
        return result > 0
    }

    // ✅ Delete Task
    fun deleteTask(taskId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_TASK,
            "$COL_ID = ?",
            arrayOf(taskId.toString())
        )
        db.close()
        return result > 0
    }


    fun updateTask(
        id: Int,
        taskName: String,
        taskDesc: String,
        taskDate: String,
        empName: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("task_name", taskName)
            put("task_desc", taskDesc)
            put("task_date", taskDate)
            put("emp_name", empName)
        }

        val result = db.update(
            "tasks",
            values,
            "id = ?",
            arrayOf(id.toString())
        )
        db.close()
        return result > 0
    }

}
