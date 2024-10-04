package com.example.bottomnavigation_japson


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class CalculatorFragment : Fragment() {

    private lateinit var display: TextView
    private var currentInput = ""
    private var operator = ""
    private var firstValue = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.calculator_fragment, container, false)

        // Initialize the display TextView
        display = view.findViewById(R.id.display)

        // Button click listeners
        setNumberButtonClickListener(view)
        setOperatorButtonClickListener(view)

        // Clear button click
        val buttonClear = view.findViewById<MaterialButton>(R.id.buttonClear)
        buttonClear.setOnClickListener {
            clear()
        }

        // Equals button click
        val buttonEqual = view.findViewById<MaterialButton>(R.id.buttonEqual)
        buttonEqual.setOnClickListener {
            calculateResult()
        }

        return view
    }

    private fun setNumberButtonClickListener(view: View) {
        val numberButtons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7,
            R.id.button8, R.id.button9
        )

        for (id in numberButtons) {
            val button = view.findViewById<MaterialButton>(id)
            button.setOnClickListener {
                appendNumber(button.text.toString())
            }
        }

        val buttonDecimal = view.findViewById<MaterialButton>(R.id.buttonDecimal)
        buttonDecimal.setOnClickListener {
            if (!currentInput.contains(".")) {
                currentInput += "."
                display.text = currentInput
            }
        }
    }

    private fun setOperatorButtonClickListener(view: View) {
        val operatorButtons = listOf(
            R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide
        )

        for (id in operatorButtons) {
            val button = view.findViewById<MaterialButton>(id)
            button.setOnClickListener {
                operator = button.text.toString()
                firstValue = currentInput.toDoubleOrNull() ?: 0.0
                currentInput = ""
            }
        }
    }

    private fun appendNumber(number: String) {
        currentInput += number
        display.text = currentInput
    }

    private fun calculateResult() {
        val secondValue = currentInput.toDoubleOrNull() ?: return
        var result = 0.0

        when (operator) {
            "+" -> result = firstValue + secondValue
            "-" -> result = firstValue - secondValue
            "*" -> result = firstValue * secondValue
            "/" -> if (secondValue != 0.0) {
                result = firstValue / secondValue
            } else {
                display.text = "Error"
                return
            }
        }

        currentInput = "%.2f".format(result)
        display.text = currentInput
        operator = ""
    }

    private fun clear() {
        currentInput = ""
        operator = ""
        firstValue = 0.0
        display.text = "0"
    }
}
