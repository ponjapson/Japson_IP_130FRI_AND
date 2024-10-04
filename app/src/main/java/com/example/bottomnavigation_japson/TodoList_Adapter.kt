package com.example.bottomnavigation_japson

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class TodoList_Adapter(private val context: Context, private val tasks: List<TodoListFragment.Task>) : BaseAdapter() {

    override fun getCount(): Int {
        return tasks.size
    }

    override fun getItem(position: Int): TodoListFragment.Task {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.todolist, parent, false)

        val task = getItem(position)

        // Get references to the views
        val taskTextView: TextView = view.findViewById(R.id.taskTextView)
        val taskImageView: ImageView = view.findViewById(R.id.taskImageView)

        // Set the task name and image
        taskTextView.text = task.name
        taskImageView.setImageResource(task.imageResId)

        // Optionally, you can set the task as done or not visually (like strikethrough)
        if (task.isDone) {
            taskTextView.paintFlags = taskTextView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            taskTextView.paintFlags = taskTextView.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        return view
    }
}
