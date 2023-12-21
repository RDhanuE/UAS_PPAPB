package com.example.calorify20.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calorify20.dataClass.Preset
import com.example.calorify20.databinding.RecyclerPresetAdminItemBinding

typealias OnClickUpdate = (Preset) -> Unit
typealias OnClickDelete = (Preset) -> Unit

class PresetAdminRvAdapter (private val listPreset: MutableList<Preset>, private val onClickUpdate: OnClickUpdate, private val onClickDelete: OnClickDelete) : RecyclerView.Adapter<PresetAdminRvAdapter.ItemAdminPresetViewHolder>() {
    inner class ItemAdminPresetViewHolder (private val binding: RecyclerPresetAdminItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Preset){
            with(binding){
                presetAdminType.setText(data.type)
                presetAdminOccasion.setText(data.occasion)
                presetAdminItem.setText(data.item)
                presetAdminUpdateButton.setOnClickListener{
                    onClickUpdate(data)
                }
                presetAdminDeleteButton.setOnClickListener{
                    onClickDelete(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdminPresetViewHolder {
        val binding = RecyclerPresetAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemAdminPresetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPreset.size
    }

    override fun onBindViewHolder(holder: ItemAdminPresetViewHolder, position: Int) {
        holder.bind(listPreset[position])
    }

}