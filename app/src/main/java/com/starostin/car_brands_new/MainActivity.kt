package com.starostin.car_brands_new

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var btnAdd: Button
    private lateinit var btnInfo: Button
    private lateinit var tvVehicleInfo: TextView

    private var vehicleList = mutableListOf(
        Vehicle("Toyota", true, 1500, 4),
        Vehicle("Honda", false, 300, 2),
        Vehicle("BMW", true, 2000, 4),
        Vehicle("Harley Davidson", false, 500, 2),
        Vehicle("Ford", true, 1800, 4)
    )

    private lateinit var adapter: CustomSpinnerAdapter

    private var currentPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner)
        btnAdd = findViewById(R.id.btn_add)
        btnInfo = findViewById(R.id.btn_info)
        tvVehicleInfo = findViewById(R.id.tv_vehicle_info)

        adapter = CustomSpinnerAdapter(this, vehicleList)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentPosition = position
                updateInfo(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnAdd.setOnClickListener {
            showAddDialog()
        }

        btnInfo.setOnClickListener {
            showInfo()
        }

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition", -1)
            if (currentPosition != -1) {
                updateInfo(currentPosition)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentPosition", currentPosition)
    }

    private fun updateInfo(position: Int) {
        val vehicle = vehicleList[position]
        val type = if (vehicle.isCar) "Автомобиль" else "Мотоцикл"
        tvVehicleInfo.text = "Наименование: ${vehicle.name}\n" +
                "Тип: $type\n" +
                "Грузоподъемность: ${vehicle.capacity}\n" +
                "Количество осей: ${vehicle.axles}"
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_vehicle, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Добавить новое транспортное средство")

        val etName = dialogView.findViewById<EditText>(R.id.et_name)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radio_group)
        val etCapacity = dialogView.findViewById<EditText>(R.id.et_capacity)
        val etAxles = dialogView.findViewById<EditText>(R.id.et_axles)

        dialogBuilder.setPositiveButton("Добавить") { _, _ ->
            val name = etName.text.toString()
            val isCar = radioGroup.checkedRadioButtonId == R.id.radio_car
            val capacity = etCapacity.text.toString().takeIf { it.isNotEmpty() }?.toIntOrNull() ?: DEFAULT_CAPACITY
            val axles = etAxles.text.toString().takeIf { it.isNotEmpty() }?.toIntOrNull() ?: DEFAULT_AXLES

            val newVehicle = Vehicle(name, isCar, capacity, axles)
            vehicleList.add(newVehicle)
            adapter.notifyDataSetChanged() // Обновляем адаптер после добавления нового элемента
        }

        dialogBuilder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }

        dialogBuilder.create().show()
    }


    private fun showInfo() {
        val position = spinner.selectedItemPosition + 1
        val totalItems = vehicleList.size

        var infoText = "Позиция $position из $totalItems\n\n"
        if (position >= 1 && position <= totalItems) {
            val vehicle = vehicleList[position - 1]
            val type = if (vehicle.isCar) "Автомобиль" else "Мотоцикл"
            infoText += "Наименование: ${vehicle.name}\n" +
                    "Тип: $type\n" +
                    "Грузоподъемность: ${vehicle.capacity}\n" +
                    "Количество осей: ${vehicle.axles}"
        }

        tvVehicleInfo.text = infoText
        tvVehicleInfo.setBackgroundColor(generateRandomColor())
    }


    private fun generateRandomColor(): Int {
        val rnd = Random
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    companion object {
        private const val DEFAULT_CAPACITY = 1000
        private const val DEFAULT_AXLES = 2
    }
}
