package com.geekbrains.weather.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.weather.R

class ContactsAdapter(private var items: List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ContactItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        return ContactItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        val contact = items[position]

        holder.itemView.apply {
            findViewById<TextView>(R.id.id_label).text = contact.id.toString()
            findViewById<TextView>(R.id.name_label).text = contact.name
            findViewById<TextView>(R.id.number_label).text = contact.number
        }
    }

    override fun getItemCount(): Int = items.size

    class ContactItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}