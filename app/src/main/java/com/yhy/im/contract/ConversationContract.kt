package com.yhy.im.contract

interface ConversationContract {

    interface Presenter: BasePresenter{
        fun loadConversation()
    }

    interface View{
        fun onStartLoadConversation()    //开始加载会话列表时调用,例如显示进度条
        fun onLoadConversationSuccess()  //加载会话列表成功时调用
        fun onLoadConversationFail()     //加载会话列表失败时调用
    }
}