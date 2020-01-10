package com.yhy.im.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hyphenate.chat.EMMessage
import com.hyphenate.util.DateUtils
import com.yhy.im.widget.ReceiveMsgItemView
import com.yhy.im.widget.SendMsgItemView

class MsgListAdapter(val context: Context, val msgList: List<EMMessage>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        val VIEW_TYPE_SEND = 100
        val VIEW_TYPE_RECEIVE = 200
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == VIEW_TYPE_SEND){
            return SendMsgViewHolder(SendMsgItemView(context))
        }else {
            return ReceiveMsgViewHolder(ReceiveMsgItemView(context))
        }
    }

    override fun getItemCount(): Int = msgList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        val msg = msgList.get(position)
        val showTimeStamp = showTimeStamp(position)
        if(type == VIEW_TYPE_SEND){
            val view = holder.itemView as SendMsgItemView
            view.bindData(msg,showTimeStamp)
        }else {
            val view = holder.itemView as ReceiveMsgItemView
            view.bindData(msg,showTimeStamp)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(msgList.get(position).direct() == EMMessage.Direct.SEND){
            return VIEW_TYPE_SEND
        }else {
            return VIEW_TYPE_RECEIVE
        }
    }

    //是否显示时间戳
    private fun showTimeStamp(position: Int): Boolean{
        var showTimeStamp = true
        if(position > 0){
            showTimeStamp = !DateUtils.isCloseEnough(msgList[position].msgTime, msgList[position - 1].msgTime)
        }
        return showTimeStamp
    }

    class SendMsgViewHolder: RecyclerView.ViewHolder{
        constructor(itemView: View?) : super(itemView)
    }

    class ReceiveMsgViewHolder: RecyclerView.ViewHolder{
        constructor(itemView: View?) : super(itemView)
    }
}