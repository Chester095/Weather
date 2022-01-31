package com.geekbrains.weather.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.weather.R

class ContactsAdapter(private var items: List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    var TAG = "!!! ContactsAdapter "
    var listener: OnItemClick? = null

    //функциональный интерфейс
    fun interface OnItemClick {
        // чтобы contact была parcelable
        // целиком объект возращаем
        fun onClick(contact: Contact)
    }

    inner class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // по name будем заполнять элемент списка
        fun bind(contact: Contact) {

            itemView.apply {
                Log.d(TAG, " itemView.apply")
                findViewById<TextView>(R.id.id_label).text = contact.id
                findViewById<TextView>(R.id.name_label).text = contact.name
                findViewById<TextView>(R.id.number_label).text = contact.number

                //когда наш Айтем вью будут кликать
                setOnClickListener {
                    Log.d(TAG, " setOnClickListener")
                    listener?.onClick(contact)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(items[position])
        Log.d(TAG, " onBindViewHolder заполняем position = $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        Log.d(TAG, " onCreateViewHolder")
        return ContactViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size
}