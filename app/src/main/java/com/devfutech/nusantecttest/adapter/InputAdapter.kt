package com.devfutech.nusantecttest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.devfutech.nusantecttest.databinding.ItemInputBinding

class InputAdapter(private val size: Int, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<InputAdapter.InputHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): InputHolder {
        val view = ItemInputBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return InputHolder(view)
    }

    override fun onBindViewHolder(holder: InputHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return size
    }

    inner class InputHolder(private val binding: ItemInputBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                inputText.addTextChangedListener {
                    it?.let {
                        if (binding.cbInput.isChecked){
                            listener.onItemAddClicked(position, it.toString().toDouble())
                        }
                    }
                }
                cbInput.setOnClickListener {
                    if (binding.cbInput.isChecked && !inputText.text.isNullOrEmpty()) {
                        listener.onItemAddClicked(position, inputText.text.toString().toDouble())
                    } else {
                        listener.onItemRemoveClicked(position)
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemAddClicked(position: Int, value: Double)
        fun onItemRemoveClicked(position: Int)
    }
}