package com.botirov.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var tasks: MutableList<Task>,private val itemLongClickListener: (Int) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTextView: TextView = itemView.findViewById(R.id.name)
        val dueTimeTextView: TextView = itemView.findViewById(R.id.dueTime)
        val completeButton: ImageButton = itemView.findViewById(R.id.completeButton)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = tasks[position]
                    val editTaskSheet = NewTaskSheet.newInstance(task) // Pass the clicked task for editing
                    editTaskSheet.show((itemView.context as AppCompatActivity).supportFragmentManager, editTaskSheet.tag)
                }
            }
            itemView.setOnLongClickListener {
                itemLongClickListener(adapterPosition)
                true  // Returning true here prevents other click events from being triggered
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskTextView.text = task.content

        if (task.isCompleted) {
            holder.completeButton.setImageResource(R.drawable.ic_checked)

        } else {
            holder.completeButton.setImageResource(R.drawable.ic_unchecked)
        }

        holder.completeButton.setOnClickListener {
            task.isCompleted = !task.isCompleted
            if (task.isCompleted) {
                Toast.makeText(holder.itemView.context, "Good job on completing the task!", Toast.LENGTH_SHORT).show()
            } else {
                // Optionally, you can have a different message when the task is unchecked, like:
                // Toast.makeText(holder.itemView.context, "Task marked as incomplete.", Toast.LENGTH_SHORT).show()
            }

            notifyItemChanged(position) // This will update the UI for the specific item
        }
    }

    fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
    }
}
