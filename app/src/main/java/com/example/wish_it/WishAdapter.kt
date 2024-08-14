package com.example.wish_it

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishAdapter(private val wishes: List<String>) :
    RecyclerView.Adapter<WishAdapter.WishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return WishViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        holder.bind(wishes[position])
    }

    override fun getItemCount(): Int = wishes.size

    inner class WishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wishTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(wish: String) {
            wishTextView.text = wish
        }
    }
}
