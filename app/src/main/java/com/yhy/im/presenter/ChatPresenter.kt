package com.yhy.im.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.yhy.im.adapter.EMCallBackAdapter
import com.yhy.im.contract.ChatContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ChatPresenter(val view: ChatContract.View): ChatContract.Presenter {

    companion object{
        //加载更多聊天记录时,一次加载的条数
        val PAGE_SIZE = 10
    }

    val msgList = mutableListOf<EMMessage>()

    override fun sendMsg(msg: String, toUser: String) {
        val emsg = EMMessage.createTxtSendMessage(msg,toUser)
        emsg.setMessageStatusCallback(object: EMCallBackAdapter(){
            override fun onSuccess() {
                onUIThread {
                    view.onSendMsgSuccess()
                }
            }

            override fun onError(p0: Int, p1: String?) {
                onUIThread {
                    view.onSendMsgFail()
                }
            }
        })
        msgList.add(emsg)

        view.onSendMsgBegin()

        EMClient.getInstance().chatManager().sendMessage(emsg)
    }

    override fun addMsg(toUser: String, list: MutableList<EMMessage>?) {
       list?.let {
           msgList.addAll(it)
       }

        //将消息置为已读
        val conversation = EMClient.getInstance().chatManager().getConversation(toUser)
        conversation.markAllMessagesAsRead()
    }

    override fun loadMsg(username: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(username)
            //将消息置为已读
            conversation.markAllMessagesAsRead()
            msgList.addAll(conversation.allMessages)
            uiThread {
                view.onAfterLoadMsg()
            }
        }
    }

    override fun loadMoreMsg(username: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(username)
            val msgId = msgList[0].msgId
            //加到最后一条消息之前
            msgList.addAll(0, conversation.loadMoreMsgFromDB(msgId,PAGE_SIZE))
            uiThread {
                view.onAfterLoadMsg()
            }
        }
    }
}