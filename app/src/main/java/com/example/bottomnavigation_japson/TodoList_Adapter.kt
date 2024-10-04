package com.example.bottomnavigation_japson

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

// Custom adapter for the ListView
class TodoList_Adapter (private val context: Context, private val items: MutableList<TodoListItem>) : BaseAdapter() {

    private var filteredItems: MutableList<TodoListItem> = items.toMutableList()

    override fun getCount(): Int {
        return filteredItems.size
    }

    override fun getItem(position: Int): Any = filteredItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            // Inflate the custom row layout
            view = LayoutInflater.from(context).inflate(R.layout.todolist, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Get the data for the current item
        val listItem = filteredItems[position]

        // Bind the data to the views
        viewHolder.imageView.setImageResource(listItem.imageResId)
        viewHolder.textView.text = listItem.description
        viewHolder.checkBox.isChecked = listItem.isChecked

        viewHolder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            listItem.isChecked = isChecked
        }

        view.setOnTouchListener(object : View.OnTouchListener {
            private var lastTouchTime: Long = 0
            private val doubleClickInterval = 300 // milliseconds

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastTouchTime < doubleClickInterval) {
                        showEditDeleteDialog(position)
                        return true
                    }
                    lastTouchTime = currentTime
                }
                return false
            }
        })

        return view
    }

    private fun showEditDeleteDialog(position: Int) {
        val listItem = filteredItems[position]
        val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_delete, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        builder.setTitle("Options")

        val editButton = dialogView.findViewById<Button>(R.id.btn_edit)
        val deleteButton = dialogView.findViewById<Button>(R.id.btn_delete)

        val dialog = builder.create()

        editButton.setOnClickListener {
            val editDialogView = LayoutInflater.from(context).inflate(R.layout.edit, null)
            val editText = editDialogView.findViewById<EditText>(R.id.editTaskDescription)
            editText.setText(listItem.description)

            val editDialogBuilder = AlertDialog.Builder(context)
            editDialogBuilder.setView(editDialogView)
            editDialogBuilder.setTitle("Edit Task")

            val saveButton = editDialogView.findViewById<Button>(R.id.btnSave)

            val editDialog = editDialogBuilder.create()
            editDialog.show()

            saveButton.setOnClickListener {
                val newDescription = editText.text.toString()
                listItem.description = newDescription
                notifyDataSetChanged()
                editDialog.dismiss()
            }

            dialog.dismiss()
        }

        deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete '${listItem.description}'?")
                .setPositiveButton("Yes") { _, _ ->
                    filteredItems.removeAt(position)
                    notifyDataSetChanged()
                    dialog.dismiss()
                    Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        }

        dialog.show()
    }

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.img)
        val textView: TextView = view.findViewById(R.id.description)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
    }

    fun filter(query: String) {
        filteredItems.clear()
        if (query.isEmpty()) {
            filteredItems.addAll(items)
        } else {
            filteredItems.addAll(items.filter { it.description.contains(query, ignoreCase = true) })
        }
        notifyDataSetChanged()
    }
}