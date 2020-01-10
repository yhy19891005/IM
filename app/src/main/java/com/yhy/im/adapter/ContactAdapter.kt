package com.yhy.im.adapter

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hyphenate.chat.EMClient
import com.yhy.im.R
import com.yhy.im.data.ContactInfo
import com.yhy.im.ui.activity.ChatActivity
import com.yhy.im.widget.ContactItemView
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onLongClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ContactAdapter(val context: Context,val contactsList: MutableList<ContactInfo>): RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContactHolder {
        return ContactHolder(ContactItemView(context))
    }

    override fun getItemCount(): Int = contactsList.size

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val info = contactsList.get(position)
        val view = holder.itemView as ContactItemView
        view.bindData(info)
        //点击进入聊天页面
        view.onClick {
            context.startActivity<ChatActivity>("name" to info.contactname)
        }
        //长按删除好友
        view.onLongClick {
            val msg = String.format(context.getString(R.string.delete_friend_message), info.contactname)
            AlertDialog.Builder(context)
                       .setMessage(msg)
                       .setNegativeButton(context.getString(R.string.cancel),null)
                       .setPositiveButton(context.getString(R.string.confirm),{
                           dialog, which ->
                           deleteContact(info)
                        })
                       .show()
        }
    }

    private fun deleteContact(info: ContactInfo){
        EMClient.getInstance().contactManager().aysncDeleteContact(info.contactname,object: EMCallBackAdapter(){

            override fun onSuccess() {
               context.runOnUiThread {
                   context.toast(R.string.delete_friend_success)
               }
            }

            override fun onError(p0: Int, p1: String?) {
                context.runOnUiThread {
                    context.toast(R.string.delete_friend_failed)
                }
            }
        })
    }

    class ContactHolder: RecyclerView.ViewHolder{
        constructor(itemView: View?) : super(itemView)
    }
}