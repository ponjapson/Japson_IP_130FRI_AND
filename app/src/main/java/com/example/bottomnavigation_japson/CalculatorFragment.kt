package com.example.bottomnavigation_japson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class CalculatorFragment : Fragment() {

    private lateinit var input1: EditText
    private lateinit var input2: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnSubtract: Button
    private lateinit var btnMultiply: Button
    private lateinit var btnDivide: Button
    private lateinit var tvResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.calculator_fragment, container, false)

        // Initialize views
        input1 = view.findViewById(R.id.input1)
        input2 = view.findViewById(R.id.input2)
        btnAdd = view.findViewById(R.id.btn_add)
        btnSubtract = view.findViewById(R.id.btn_subtract)
        btnMultiply = view.findViewById(R.id.btn_multiply)
        btnDivide = view.findViewById(R.id.btn_divide)
        tvResult = view.findViewById(R.id.tv_result)

        // Set onClickListeners
        btnAdd.setOnClickListener { calculate("+") }
        btnSubtract.setOnClickListener { calculate("-") }
        btnMultiply.setOnClickListener { calculate("*") }
        btnDivide.setOnClickListener { calculate("/") }

        return view
    }

    private fun calculate(operation: String) {
        val num1 = input1.text.toString().toDoubleOrNull()
        val num2 = input2.text.toString().toDoubleOrNull()

        if (num1 != null && num2 != null) {
            val result = when (operation) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN // Handle division by zero
                else -> 0.0
            }
            tvResult.text = "Result: $result"
        } else {
            tvResult.text = "Result: Invalid Input"
        }
    }
}
