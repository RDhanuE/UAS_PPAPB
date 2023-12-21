package com.example.calorify20.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calorify20.databinding.RecyclerActivityItemBinding
import com.example.calorify20.room.InOutTake

class ActivityRvAdapter (private val listActivity: List<InOutTake>) : RecyclerView.Adapter<ActivityRvAdapter.ItemActivityViewHolder>(){
    inner class ItemActivityViewHolder (private val binding: RecyclerActivityItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: InOutTake) {
            with(binding) {
                activityType.setText(data.type)
                activityTime.setText(data.time)
                activityOccasion.setText(data.occasion)
                activityItem.setText(data.item)
                activityCalory.setText(data.calory.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemActivityViewHolder {
        val binding =
            RecyclerActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemActivityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listActivity.size
    }

    override fun onBindViewHolder(holder: ItemActivityViewHolder, position: Int) {
        holder.bind(listActivity[position])
    }

}