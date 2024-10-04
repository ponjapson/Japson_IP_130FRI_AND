package com.example.bottomnavigation_japson

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment

class TodoListFragment : Fragment() {

    private lateinit var edtSearch: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.todolist_fragment, container, false)

        edtSearch = view.findViewById(R.id.edtSearch)
        val itemList = mutableListOf(
            TodoListItem(R.drawable.ride, "Ride for Fun!", false),
            TodoListItem(R.drawable.student, "I will finish my Studies!", true),
            TodoListItem(R.drawable.gym, "Workout for health.", false),
            TodoListItem(R.drawable.work, "Work for bills payment or to survive.", false),
            TodoListItem(R.drawable.laba, "Do Laundry every weekend.", false)
        )

        // Find the ListView and set the adapter
        val listView = view.findViewById<ListView>(R.id.toDoListView)
        val adapter = TodoList_Adapter(requireContext(), itemList)
        listView.adapter = adapter

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
        })

        return view
    }
}