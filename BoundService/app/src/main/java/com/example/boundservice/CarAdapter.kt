package com.example.boundservice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_car.view.*

class CarAdapter(private var car: MutableList<CarData>): RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private var onClickItem: ((CarData) -> Unit?)? = null

    fun addItem(item: MutableList<CarData>){
        this.car = item
        notifyDataSetChanged()
    }

    fun delete(item: MutableList<CarData>){
        this.car = item
        notifyDataSetChanged()
    }

    fun upDateSql(item: MutableList<CarData>){
        this.car = item
        notifyDataSetChanged()
    }

    fun clickItem(callback: (CarData)-> Unit){
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_car, parent, false)
        return CarViewHolder(v)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val currentItem = car[position]
        holder.bindHolder(currentItem)
        holder.itemView.setOnClickListener {
            onClickItem?.invoke(currentItem)
        }
    }

    override fun getItemCount() = car.size

    inner class CarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindHolder(cars: CarData){
            cars.run {
                itemView.let {
                    it.textId.text = id.toString()
                    it.textNameCar.text = nameCar
                    it.textColorCar.text = colorCar
                }
            }
        }
    }
}