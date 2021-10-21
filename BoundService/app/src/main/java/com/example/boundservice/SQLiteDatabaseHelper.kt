package com.example.boundservice

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1){

    companion object{
        private const val DATABASE_NAME = "db_car"
        private const val TABLE_CAR = "tb_car"
        private const val ID_CAR = "id_car"
        private const val NAME_CAR = "name_car"
        private const val COLOR_CAR = "color_car"
        private const val DB_CAR = "CREATE TABLE $TABLE_CAR($ID_CAR INTEGER PRIMARY KEY, " +
                "$NAME_CAR TEXT, " +
                "$COLOR_CAR TEXT)"

        private var instance: SQLiteDatabaseHelper? = null

        fun getInstance(context: Context): SQLiteDatabaseHelper{
            return instance ?: SQLiteDatabaseHelper(context).also { instance = it }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DB_CAR)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun insert(car: CarData){
        val contentValue = ContentValues()
        contentValue.apply {
            put(ID_CAR, car.id)
            put(NAME_CAR, car.nameCar)
            put(COLOR_CAR, car.colorCar)
        }.run {
            instance?.writableDatabase?.insert(TABLE_CAR, null, this)
        }
    }

    fun getCar():MutableList<CarData>{
        val cars = mutableListOf<CarData>()
        val cursor: Cursor? = instance?.readableDatabase?.query(
            TABLE_CAR,
            null,
            null, null,
            null, null,
            null)

        var id: Int
        var nameC: String
        var colorC: String

        if (cursor!!.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id_car"))
                nameC = cursor.getString(cursor.getColumnIndex("name_car"))
                colorC = cursor.getString(cursor.getColumnIndex("color_car"))

                val carData = CarData(id = id, nameCar = nameC, colorCar = colorC)
                cars.add(carData)
            }while (cursor.moveToNext())

            cursor.close()
        }
        return cars
    }

    fun removeCar(id: Int){
        instance?.writableDatabase?.delete(TABLE_CAR, "$ID_CAR =?", arrayOf(id.toString()))
    }

    fun upDateCar(rowId: String, name_car: String, color_car: String) {
        val contentValues = ContentValues()
        contentValues.put(NAME_CAR, name_car)
        contentValues.put(COLOR_CAR, color_car)
        instance?.writableDatabase?.update(TABLE_CAR, contentValues, "$ID_CAR =?", arrayOf(rowId))
    }
}



