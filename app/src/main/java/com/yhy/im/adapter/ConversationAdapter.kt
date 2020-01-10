package com.yhy.im.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hyphenate.chat.EMConversation
import com.yhy.im.ui.activity.ChatActivity
import com.yhy.im.widget.ConversationItemView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class ConversationAdapter(val context: Context, val conversations: List<EMConversation>): RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ConversationViewHolder {
        return ConversationViewHolder(ConversationItemView(context))
    }

    override fun getItemCount(): Int = conversations.size

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations.get(position)
        val view = holder.itemView as ConversationItemView
        view.bindData(conversation)

        view.onClick {
            context.startActivity<ChatActivity>("name" to conversation.conversationId())
        }
    }

    class ConversationViewHolder: RecyclerView.ViewHolder{
        constructor(itemView: View?) : super(itemView)
    }
}