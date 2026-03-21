package com.example.taskflow


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.Model.TaskModel

class TaskAdapter(
    private var taskList: ArrayList<TaskModel>,
    private val onStatusChanged: (TaskModel, Int) -> Unit,
    private val onDeleteClicked: (TaskModel) -> Unit,
    private val onEditClicked: (TaskModel) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTaskName: TextView = itemView.findViewById(R.id.tvTaskName)
        val tvTaskDesc: TextView = itemView.findViewById(R.id.tvTaskDesc)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvEmpName: TextView = itemView.findViewById(R.id.tvEmpName)
        val cbStatus: CheckBox = itemView.findViewById(R.id.cbStatus)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        val btnEditTask: ImageButton = itemView.findViewById(R.id.btnedit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.tvTaskName.text = task.taskName
        holder.tvTaskDesc.text = task.taskDesc
        holder.tvDate.text = task.taskDate
        holder.tvEmpName.text = task.empName


        holder.cbStatus.setOnCheckedChangeListener(null)
        holder.cbStatus.isChecked = task.status == 1

        holder.cbStatus.text = if (task.status == 1) "Completed" else "Incomplete"

        holder.cbStatus.setOnCheckedChangeListener { _, isChecked ->
            val newStatus = if (isChecked) 1 else 0
            onStatusChanged(task, newStatus)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClicked(task)
        }

        holder.btnEditTask.setOnClickListener {
            onEditClicked(task)
        }
    }

    override fun getItemCount(): Int = taskList.size

    fun updateList(newList: ArrayList<TaskModel>) {
        taskList = newList
        notifyDataSetChanged()
    }
}
