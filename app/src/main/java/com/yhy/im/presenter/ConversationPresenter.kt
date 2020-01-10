package com.yhy.im.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.exceptions.HyphenateException
import com.yhy.im.contract.ConversationContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ConversationPresenter(val view: ConversationContract.View): ConversationContract.Presenter {

    val conversations = mutableListOf<EMConversation>()

    override fun loadConversation() {
        doAsync {
            try {
                val allConversations =  EMClient.getInstance().chatManager().allConversations
                conversations.clear()
                conversations.addAll(allConversations.values)
                // sortBy升序排列 sortByDescending降序排列
                conversations.sortByDescending {
                    it.lastMessage.msgTime
                }
                uiThread {
                    view.onLoadConversationSuccess()
                }
            } catch (e: HyphenateException) {
                uiThread {
                    view.onLoadConversationFail()
                }
            }
        }

    }
}