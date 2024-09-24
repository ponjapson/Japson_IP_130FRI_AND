package com.example.todolist_japson

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class MainActivity : AppCompatActivity() {
    private lateinit var taskListView: ListView
    private lateinit var taskEditText: EditText
    private lateinit var addTaskButton: Button
    private val tasks = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskListView = findViewById(R.id.taskListView)
        taskEditText = findViewById(R.id.taskEditText)
        addTaskButton = findViewById(R.id.addTaskButton)

        // Add a default task
        tasks.add(Task("Task 1", false, R.drawable.ic_launcher_foreground))

        adapter = TaskAdapter()
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
        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val position = taskListView.pointToPosition(e!!.x.toInt(), e.y.toInt())
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
    }

    // Show dialog with options to edit or delete the task
    private fun showEditDeleteDialog(position: Int) {
        val task = tasks[position]
        val options = arrayOf("Edit Task", "Delete Task")

        AlertDialog.Builder(this)
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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_task, null)
        val taskEditText = dialogView.findViewById<EditText>(R.id.dialogTaskEditText)

        taskEditText.setText(task.name) // Pre-fill with current task name

        AlertDialog.Builder(this)
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

    // Custom adapter for tasks
    inner class TaskAdapter : BaseAdapter() {
        override fun getCount(): Int = tasks.size
        override fun getItem(position: Int): Any = tasks[position]
        override fun getItemId(position: Int): Long = position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(this@MainActivity)
                .inflate(R.layout.list_items, parent, false)

            val task = tasks[position]

            val checkBox = view.findViewById<CheckBox>(R.id.taskCheckBox)
            val taskTextView = view.findViewById<TextView>(R.id.taskTextView)
            val taskImageView = view.findViewById<ImageView>(R.id.taskImageView)

            taskTextView.text = task.name
            checkBox.isChecked = task.isDone
            taskImageView.setImageResource(task.imageResId)

            // Handle checkbox toggle
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                task.isDone = isChecked
            }

            return view
        }
    }
}