package com.yhy.im.contract

import com.hyphenate.chat.EMMessage

interface ChatContract {

    interface Presenter: BasePresenter{
        // msg表示消息内容, toUser表示消息接收者用户名
        fun sendMsg(msg: String, toUser: String)
        // 接收到消息后的回调
        fun addMsg(toUser: String, list: MutableList<EMMessage>?)
        // 加载聊天记录
        fun loadMsg(username: String)
        // 加载更多聊天记录
        fun loadMoreMsg(username: String)
    }

    interface View{
        fun onSendMsgBegin()    //开始发送消息时调用
        fun onSendMsgSuccess()  //发送消息成功时调用
        fun onSendMsgFail()     //发送消息失败时调用
        fun onAfterLoadMsg()    //加载聊天记录后调用
    }
}