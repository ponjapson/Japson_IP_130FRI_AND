package com.example.calculator_japson1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var input1: EditText
    private lateinit var input2: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnSubtract: Button
    private lateinit var btnMultiply: Button
    private lateinit var btnDivide: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input1 = findViewById(R.id.input1)
        input2 = findViewById(R.id.input2)
        btnAdd = findViewById(R.id.btn_add)
        btnSubtract = findViewById(R.id.btn_subtract)
        btnMultiply = findViewById(R.id.btn_multiply)
        btnDivide = findViewById(R.id.btn_divide)
        tvResult = findViewById(R.id.tv_result)

        btnAdd.setOnClickListener { calculate("+") }
        btnSubtract.setOnClickListener { calculate("-") }
        btnMultiply.setOnClickListener { calculate("*") }
        btnDivide.setOnClickListener { calculate("/") }
    }

    private fun calculate(operation: String) {
        val num1 = input1.text.toString().toDoubleOrNull()
        val num2 = input2.text.toString().toDoubleOrNull()

        if (num1 != null && num2 != null) {
            val result = when (operation) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> num1 / num2
                else -> 0.0
            }
            tvResult.text = "Result: $result"
        } else {
            tvResult.text = "Result: Invalid Input"
        }
    }
}
