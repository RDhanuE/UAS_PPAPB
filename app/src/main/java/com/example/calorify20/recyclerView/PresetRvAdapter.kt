package com.example.calorify20.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calorify20.dataClass.Preset
import com.example.calorify20.databinding.RecyclerPresetItemBinding

typealias OnClickAdd = (Preset) -> Unit

class PresetRvAdapter (private val listPreset: MutableList<Preset>, private val onClickAdd: OnClickAdd) : RecyclerView.Adapter<PresetRvAdapter.ItemPresetViewHolder>() {
    inner class ItemPresetViewHolder (private val binding: RecyclerPresetItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Preset){
            with(binding){
                presetType.setText(data.type)
                presetOccasion.setText(data.occasion)
                presetItem.setText(data.item)
                presetContainer.setOnClickListener{
                    onClickAdd(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPresetViewHolder {
        val binding =
            RecyclerPresetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemPresetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPreset.size
    }

    override fun onBindViewHolder(holder: ItemPresetViewHolder, position: Int) {
        holder.bind(listPreset[position])
    }

}