package com.example.simplecalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.simplecalculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var firstNumber: String = ""
    private var secondNumber: String = ""
    private var operator: String = ""
    private var isOperatorSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        allClear()
    }

    fun numberAction(view: View) {
        val button = view as Button
        val value = button.text.toString()
        if (!isOperatorSelected) {
            // Entering first number
            if (value == "." && firstNumber.contains(".")) return
            firstNumber += value
            binding.workingsTV.text = firstNumber
        } else {
            // Entering second number
            if (value == "." && secondNumber.contains(".")) return
            secondNumber += value
            binding.workingsTV.text = firstNumber + " " + operator + " " + secondNumber
        }
    }

    fun operatorAction(view: View) {
        val button = view as Button
        val op = button.text.toString()
        if (firstNumber.isEmpty()) {
            Toast.makeText(this, "Enter first number", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isOperatorSelected) {
            operator = when (op) {
                "x" -> "*"
                else -> op
            }
            isOperatorSelected = true
            binding.workingsTV.text = firstNumber + " " + operator
        } else {
            // Operator already selected, allow changing operator before entering second number
            if (secondNumber.isEmpty()) {
                operator = when (op) {
                    "x" -> "*"
                    else -> op
                }
                binding.workingsTV.text = firstNumber + " " + operator
            }
        }
    }

    fun allClearAction(view: View) {
        allClear()
    }

    private fun allClear() {
        firstNumber = ""
        secondNumber = ""
        operator = ""
        isOperatorSelected = false
        binding.resultsTV.text = ""
        binding.workingsTV.text = ""
    }

    fun equalsAction(view: View) {
        if (firstNumber.isEmpty() || operator.isEmpty() || secondNumber.isEmpty()) {
            Toast.makeText(this, "Incomplete expression", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val result = calculate(firstNumber, secondNumber, operator)
            binding.resultsTV.text = result
        } catch (e: Exception) {
            binding.resultsTV.text = "Error"
        }
    }

    private fun calculate(num1: String, num2: String, op: String): String {
        val isInt1 = !num1.contains(".")
        val isInt2 = !num2.contains(".")
        val n1 = num1.toDouble()
        val n2 = num2.toDouble()
        val result = when (op) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "*" -> n1 * n2
            "/" -> if (n2 != 0.0) n1 / n2 else return "Div by 0"
            else -> return "Error"
        }
        return if (isInt1 && isInt2 && result % 1.0 == 0.0) result.toInt().toString() else result.toString()
    }

    fun backSpaceAction(view: View) {
        if (!isOperatorSelected) {
            if (firstNumber.isNotEmpty()) {
                firstNumber = firstNumber.dropLast(1)
                binding.workingsTV.text = firstNumber
            }
        } else if (secondNumber.isNotEmpty()) {
            secondNumber = secondNumber.dropLast(1)
            binding.workingsTV.text = firstNumber + " " + operator + " " + secondNumber
        } else if (operator.isNotEmpty()) {
            operator = ""
            isOperatorSelected = false
            binding.workingsTV.text = firstNumber
        }
    }
}