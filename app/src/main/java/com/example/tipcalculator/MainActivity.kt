package com.example.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.text.NumberFormat

class MainActivity : AppCompatActivity(), View.OnFocusChangeListener,
    SeekBar.OnSeekBarChangeListener {

    private lateinit var txtAmountOfPeople: EditText
    private lateinit var txtSubtotal: EditText
    private lateinit var skTaxPercent: SeekBar
    private lateinit var table: Table
    val formatter = NumberFormat.getCurrencyInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtAmountOfPeople = this.findViewById<EditText>(R.id.inputPeopleAmount)
        txtAmountOfPeople.setOnFocusChangeListener(this)

        txtSubtotal = this.findViewById<EditText>(R.id.inputSubTotal)
        txtSubtotal.setOnFocusChangeListener(this)

        skTaxPercent = this.findViewById<SeekBar>(R.id.serviceTaxSeekBar)
        skTaxPercent.progress = 10
        skTaxPercent.setOnSeekBarChangeListener(this)
    }

    private fun updateBill() {
        val data: List<String> = getData()

        if (validateData(data[0], data[1])) {
            table = Table(data[0].toInt(), data[1].toDouble())
            val bill = calculateBill(table)
            writeTableTotalBill(bill.total)
            writeBillForEach(bill.totalForEach)
        } else {
            printNoDataMessage()
        }
    }

    private fun getData(): List<String> {
        val customers = txtAmountOfPeople.text.toString()
        val subtotal = txtSubtotal.text.toString()

        return listOf<String>(customers, subtotal)
    }

    private fun printNoDataMessage() {
        Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show()
    }

    private fun validateData(customers: String, subtotal: String): Boolean {
        return (customers.isNotEmpty() && subtotal.isNotEmpty()) && (customers.toInt() > 0 && subtotal.toDouble() > 0.00)
    }

    private fun calculateBill(table: Table): Table {
        val subtotal = table.subtotal
        table.total = subtotal + (subtotal * (skTaxPercent.progress.toDouble() / 100))
        table.totalForEach = table.total / table.customers
        return table
    }

    private fun writeTableTotalBill(total: Double) {
        val tableTotal = this.findViewById<TextView>(R.id.tableTotalWithTax)
        tableTotal.text = formatter.format(total)
    }

    private fun writeBillForEach(total: Double) {
        val totalPerPerson = this.findViewById<TextView>(R.id.totalPerPerson)
        totalPerPerson.text = formatter.format(total)
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        // this.updateBill()
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        val servicePercent = this.findViewById<TextView>(R.id.lblServicePercent)
        servicePercent.text = skTaxPercent.progress.toString() + " %"
        this.updateBill()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }
}

class Table(var customers: Int, var subtotal: Double) {
    var total: Double = 0.0
    var totalForEach: Double = 0.0
}