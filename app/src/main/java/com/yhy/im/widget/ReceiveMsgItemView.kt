package com.yhy.im.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import com.yhy.im.R
import kotlinx.android.synthetic.main.view_receive_message_item.view.*
import java.util.*

class ReceiveMsgItemView: RelativeLayout {

    constructor(context: Context?) : this(context,null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        View.inflate(context, R.layout.view_receive_message_item, this)
    }

    fun bindData(msg: EMMessage, showTimeStamp: Boolean) {
        if(showTimeStamp){
            timestamp.visibility = View.VISIBLE
            timestamp.text = DateUtils.getTimestampString(Date(msg.msgTime))
        }else {
            timestamp.visibility = View.GONE
        }

        if(msg.type == EMMessage.Type.TXT){
            receiveMessage.text = (msg.body as EMTextMessageBody).message
        }else {
            receiveMessage.text = context.getString(R.string.no_text_message)
        }
    }
}