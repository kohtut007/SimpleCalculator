package com.example.simplecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.apply {
            var numberButtons = listOf(
                btnZero,
                btnOne,
                btnTwo,
                btnThree,
                btnFour,
                btnFive,
                btnSix,
                btnSeven,
                btnEight,
                btnNine,
                btnDot
            )

            for (btn in numberButtons) {
                btn.setOnClickListener { numberAction(btn) }
            }
        }

        binding.btnPlus.setOnClickListener { operatorAction(binding.btnPlus) }
        binding.btnMinus.setOnClickListener { operatorAction(binding.btnMinus) }
        binding.btnDivide.setOnClickListener { operatorAction(binding.btnDivide) }
        binding.btnMutiply.setOnClickListener { operatorAction(binding.btnMutiply) }

        binding.btnEqual.setOnClickListener { equalsAction() }
        binding.btnAllClear.setOnClickListener { allClear() }
        binding.btnBackSpace.setOnClickListener { backSpaceAction() }
    }

    private fun numberAction(button: Button) {
        val value = button.text.toString()
        if (!isOperatorSelected) {
            if (value == "." && firstNumber.contains(".")) return
            firstNumber += value
            binding.tvDisplays.text = firstNumber
        } else {
            if (value == "." && secondNumber.contains(".")) return
            secondNumber += value
            binding.tvDisplays.text = firstNumber + " " + operator + " " + secondNumber
        }
    }

    private fun operatorAction(button: Button) {
        val op = button.text.toString()
        if (firstNumber.isEmpty()) {
            Toast.makeText(this, "Enter first number", Toast.LENGTH_SHORT).show()
            return
        }
        val opSymbol = when (op) {
            "×" -> "*"
            else -> op
        }
        if (!isOperatorSelected) {
            operator = opSymbol
            isOperatorSelected = true
            binding.tvDisplays.text = firstNumber + " " + op
        } else {
            if (secondNumber.isEmpty()) {
                operator = opSymbol
                binding.tvDisplays.text = firstNumber + " " + op
            }
        }
    }

    private fun allClear() {
        firstNumber = ""
        secondNumber = ""
        operator = ""
        isOperatorSelected = false
        binding.tvResult.text = ""
        binding.tvDisplays.text = ""
    }

    private fun equalsAction() {
        if (firstNumber.isEmpty() || operator.isEmpty() || secondNumber.isEmpty()) {
            Toast.makeText(this, "Incomplete expression", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val result = calculate(firstNumber, secondNumber, operator)
            if (result == "Undefined" || result == "Error") {
                binding.tvResult.setTextColor(getColor(R.color.red))
            } else {
                binding.tvResult.setTextColor(getColor(R.color.white))
            }
            binding.tvResult.text = result
        } catch (e: Exception) {
            binding.tvResult.setTextColor(getColor(R.color.red))
            binding.tvResult.text = "Error"
            binding.tvDisplays.text = ""
        }
    }

    private fun calculate(num1: String, num2: String, op: String): String {
        val isFirstInt = !num1.contains(".")
        val isSecondInt = !num2.contains(".")
        val n1 = num1.toDouble()
        val n2 = num2.toDouble()
        val result = when (op) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "*" -> n1 * n2
            "/" -> if (n2 != 0.0) n1 / n2 else return "Undefined"
            else -> return "Error"
        }
        return if (isFirstInt && isSecondInt && result % 1.0 == 0.0) result.toInt().toString() else result.toString()
    }

    private fun backSpaceAction() {
        if (!isOperatorSelected) {
            if (firstNumber.isNotEmpty()) {
                firstNumber = firstNumber.dropLast(1)
                binding.tvDisplays.text = firstNumber
            }
        } else if (secondNumber.isNotEmpty()) {
            secondNumber = secondNumber.dropLast(1)
            binding.tvDisplays.text = firstNumber + " " + operator + " " + secondNumber
        } else if (operator.isNotEmpty()) {
            operator = ""
            isOperatorSelected = false
            binding.tvDisplays.text = firstNumber
        }
    }
}