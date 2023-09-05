package com.botirov.tasklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    // Assuming you have a list of tasks to display in the RecyclerView
    private val tasksList = mutableListOf<Task>()

    // Assuming you've created an adapter for your RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tasksList.addAll(loadTasksFromPreferences())


        // Set up RecyclerView
        val tasksRecyclerView: RecyclerView = findViewById(R.id.tasksRecyclerView)
        taskAdapter = TaskAdapter(tasksList) { position ->
            showDeleteConfirmationDialog(position)
        }
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.adapter = taskAdapter
        taskAdapter.notifyDataSetChanged()
        // Set up the + button click listener
        val addTaskButton: View = findViewById(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            showNewTaskSheet()
        }
    }

    private fun showNewTaskSheet() {
        val newTaskSheet = NewTaskSheet()
        newTaskSheet.show(supportFragmentManager, newTaskSheet.tag)
    }

    private fun saveTasksToPreferences() {
        val sharedPreferences = getSharedPreferences("task_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonTasks = gson.toJson(tasksList)
        editor.putString("task_list", jsonTasks)
        editor.apply()
    }

    private fun loadTasksFromPreferences(): MutableList<Task> {
        val sharedPreferences = getSharedPreferences("task_preferences", MODE_PRIVATE)
        val gson = Gson()
        val jsonTasks = sharedPreferences.getString("task_list", "")
        if (jsonTasks?.isEmpty() == true) {
            return mutableListOf()
        } else {
            val type = object : TypeToken<List<Task>>() {}.type
            return gson.fromJson(jsonTasks, type)
        }
    }

    // This function can be used to add a new task to your tasksList and notify the adapter
    fun addNewTask(task: Task, position: Int? = null) {
        if (position != null) {
            tasksList[position] = task
            taskAdapter.notifyItemChanged(position)
        } else {
            tasksList.add(task)
            taskAdapter.notifyItemInserted(tasksList.size - 1)
        }
        saveTasksToPreferences()
    }
    fun showDeleteConfirmationDialog(taskPosition: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Task")
        builder.setMessage("Are you sure you want to delete this task?")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteTask(taskPosition)
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    fun deleteTask(position: Int) {
        tasksList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        saveTasksToPreferences()
    }
    override fun onStop() {
        super.onStop()
        saveTasksToPreferences()
    }

}