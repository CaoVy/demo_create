package com.example.boundservice

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sqlite.*

class SQLiteActivity : AppCompatActivity() {

    private var carData = mutableListOf<CarData>()
    private lateinit var sqLiteDatabase: SQLiteDatabaseHelper
    private var sqlCar: CarData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)

        sqLiteDatabase = SQLiteDatabaseHelper.getInstance(this)

        val adapter = CarAdapter(carData)
        recyclerView.adapter = adapter

        adapter.clickItem {
            Toast.makeText(this, it.nameCar, Toast.LENGTH_SHORT).show()
            editCarName.setText(it.nameCar)
            editColorCar.setText(it.colorCar)
            sqlCar = it
        }

        btnAdd.setOnClickListener {
            val nCar = editCarName.text.toString()
            val cCar = editColorCar.text.toString()
            if (nCar.isEmpty() || cCar.isEmpty()) {
                Toast.makeText(this, "Please enter requried filed", Toast.LENGTH_SHORT).show()
            } else {
                val cars = CarData(
                    id = editId.text.toString().toInt(),
                    nameCar = nCar,
                    colorCar = cCar
                )
                sqLiteDatabase.insert(cars)
            }
            val sqlAdd = sqLiteDatabase.getCar()
            adapter.addItem(sqlAdd)
        }

        btnRemove.setOnClickListener {

            val id = editId.text.toString().toInt()
            sqLiteDatabase.removeCar(id = id)
            val sqlDelete = sqLiteDatabase.getCar()
            adapter.delete(sqlDelete)
        }

        btnFind.setOnClickListener {
            val sqlFind = sqLiteDatabase.getCar()
            adapter.addItem(sqlFind)
        }

        btnUpdate.setOnClickListener {
            val idCar = editId.text.toString()
            val nCar = editCarName.text.toString()
            val cCar = editColorCar.text.toString()

            if (nCar == sqlCar?.nameCar || cCar == sqlCar?.colorCar){
                Toast.makeText(this, "Vui long nhap du lieu muon thay doi", Toast.LENGTH_SHORT).show()
            }else{
                sqLiteDatabase.upDateCar(idCar, nCar, cCar)
            }
        }
    }
}


