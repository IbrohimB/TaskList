package com.botirov.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton

class NewTaskSheet : BottomSheetDialogFragment() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var descEditText: TextInputEditText
    private lateinit var saveButton: MaterialButton

    companion object {
        private const val ARG_TASK = "arg_task"
        private const val ARG_POSITION = "arg_position"

        fun newInstance(task: Task, position: Int? = null): NewTaskSheet {
            val args = Bundle()
            args.putSerializable(ARG_TASK, task)
            if (position != null) args.putInt(ARG_POSITION, position)
            val fragment = NewTaskSheet()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_task_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        nameEditText = view.findViewById(R.id.name)
        descEditText = view.findViewById(R.id.desc)
        saveButton = view.findViewById(R.id.saveButton)

        val task: Task? = arguments?.getSerializable(ARG_TASK) as Task?
        if (task != null) {
            nameEditText.setText(task.content)
            descEditText.setText(task.description)
        }

        val position: Int? = arguments?.getInt(ARG_POSITION)
        // Set click listener to save button
        saveButton.setOnClickListener {
            val taskName = nameEditText.text.toString().trim()
            val taskDescription = descEditText.text.toString().trim()

            if (taskName.isNotEmpty()) {
                val newTask = Task(content = taskName, description = taskDescription)
                (activity as MainActivity).addNewTask(newTask, position)
                dismiss()
            } else {
                Toast.makeText(context, "Task name can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveTask(name: String, desc: String) {
        // Logic to save the task (e.g., add to your list or database)
        // You may want to communicate back to your MainActivity or hosting fragment
        // to update the list. This can be done through interfaces, LiveData, or other methods.
    }
}
