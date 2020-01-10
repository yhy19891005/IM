package com.yhy.im.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import com.yhy.im.R
import kotlinx.android.synthetic.main.view_conversation_item.view.*
import java.util.*

class ConversationItemView: RelativeLayout {

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        View.inflate(context, R.layout.view_conversation_item, this)
    }

    fun bindData(conversation: EMConversation) {
        userName.text = conversation.conversationId()

        val eMsg = conversation.lastMessage
        if(eMsg.type == EMMessage.Type.TXT){
            lastMessage.text = (eMsg.body as EMTextMessageBody).message
        }else {
            lastMessage.text = context.getString(R.string.no_text_message)
        }

        timestamp.text = DateUtils.getTimestampString(Date(eMsg.msgTime))

        if(conversation.unreadMsgCount > 0){
            unreadCount.visibility = View.VISIBLE
            unreadCount.text = conversation.unreadMsgCount.toString()
        }else {
            unreadCount.visibility = View.GONE
        }

    }
}