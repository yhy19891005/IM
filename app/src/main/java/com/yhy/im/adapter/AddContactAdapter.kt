package com.yhy.im.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.yhy.im.data.AddContactInfo
import com.yhy.im.widget.AddContactItemView

class AddContactAdapter(val context: Context, val list: MutableList<AddContactInfo>): RecyclerView.Adapter<AddContactAdapter.AddContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AddContactViewHolder {
       return AddContactViewHolder(AddContactItemView(context))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AddContactViewHolder, position: Int) {
        val info = list.get(position)
        val view = holder.itemView as AddContactItemView
        view.bindData(info)
    }

    class AddContactViewHolder: RecyclerView.ViewHolder{
        constructor(itemView: View?) : super(itemView)
    }
}