package com.example.favouritetheme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView

class FavouriteThemeAdapter(
    private val users: ArrayList<String>
) : RecyclerView.Adapter<FavouriteThemeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val rootView =
            getLayoutInflater(parent.context).inflate(R.layout.item_view, parent, false)
        return MyViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cityTextView.text = users[position]
    }

    @VisibleForTesting
    internal fun getLayoutInflater(context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityTextView: TextView = itemView.findViewById(R.id.user_textview)
    }
}

