package com.example.bottomnavigation_japson

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.view.GestureDetectorCompat


class TodoListFragment : Fragment() {
    private lateinit var taskListView: ListView
    private lateinit var taskEditText: EditText
    private lateinit var addTaskButton: Button
    private val tasks = mutableListOf<Task>()
    private lateinit var adapter: TodoList_Adapter // Ensure the adapter class is properly named
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.todolist_fragment, container, false)

        taskListView = view.findViewById(R.id.taskListView)
        taskEditText = view.findViewById(R.id.taskEditText)
        addTaskButton = view.findViewById(R.id.addTaskButton)

        // Add a default task
        tasks.add(Task("Task 1", false, R.drawable.ic_launcher_foreground))

        adapter = TodoList_Adapter(requireContext(), tasks) // Updated to use the new adapter
        taskListView.adapter = adapter

        // Add a new task when the button is clicked
        addTaskButton.setOnClickListener {
            val taskText = taskEditText.text.toString()
            if (taskText.isNotEmpty()) {
                tasks.add(Task(taskText, false, R.drawable.ic_launcher_foreground))
                taskEditText.text.clear()
                adapter.notifyDataSetChanged()
            }
        }

        // Set up gesture detector for double tap
        gestureDetector = GestureDetectorCompat(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val position = taskListView.pointToPosition(e.x.toInt(), e.y.toInt())
                if (position != AdapterView.INVALID_POSITION) {
                    showEditDeleteDialog(position) // Show dialog on double tap
                }
                return super.onDoubleTap(e)
            }
        })

        taskListView.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            v.performClick()
        }

        return view // Return the inflated view
    }

    // Show dialog with options to edit or delete the task
    private fun showEditDeleteDialog(position: Int) {
        val task = tasks[position]
        val options = arrayOf("Edit Task", "Delete Task")

        AlertDialog.Builder(requireContext()) // Use requireContext() for dialog
            .setTitle("Choose an option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditTaskDialog(position) // Edit Task
                    1 -> deleteTask(position)         // Delete Task
                }
            }
            .show()
    }

    // Show a dialog to edit the task
    private fun showEditTaskDialog(position: Int) {
        val task = tasks[position]
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.todolist_dialog_task, null)
        val taskEditText = dialogView.findViewById<EditText>(R.id.dialogTaskEditText)

        taskEditText.setText(task.name) // Pre-fill with current task name

        AlertDialog.Builder(requireContext()) // Use requireContext() for dialog
            .setTitle("Edit Task")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                task.name = taskEditText.text.toString() // Update task name
                adapter.notifyDataSetChanged() // Notify adapter of data change
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Delete the task
    private fun deleteTask(position: Int) {
        tasks.removeAt(position)
        adapter.notifyDataSetChanged() // Notify adapter of data change
    }

    // Task data class
    data class Task(var name: String, var isDone: Boolean, val imageResId: Int)
}
