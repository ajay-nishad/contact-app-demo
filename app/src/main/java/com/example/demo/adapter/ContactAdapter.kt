package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.model.Contact
import kotlinx.android.synthetic.main.item_list.view.*

class ContactAdapter(private val contactList: MutableList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.MYViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MYViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MYViewHolder(view)
    }
    
    override fun getItemCount(): Int = contactList.size
    
    override fun onBindViewHolder(holder: MYViewHolder, position: Int) {
        holder.contactName.text = contactList[position].name
        holder.contactNumber.text = contactList[position].number
    }
    
    inner class MYViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName = view.contactName
        val contactNumber = view.contactNumber
    }
}