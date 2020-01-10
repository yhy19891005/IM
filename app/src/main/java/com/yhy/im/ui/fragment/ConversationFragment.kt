package com.yhy.im.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.yhy.im.R
import com.yhy.im.adapter.ConversationAdapter
import com.yhy.im.adapter.EMMessageListenerAdapter
import com.yhy.im.base.BaseFragment
import com.yhy.im.contract.ConversationContract
import com.yhy.im.presenter.ConversationPresenter
import kotlinx.android.synthetic.main.fragment_conversation.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.support.v4.toast

//消息Fragment
class ConversationFragment: BaseFragment(), ConversationContract.View{

    val mPresenter by lazy { ConversationPresenter(this) }

    val mEMMgsListener = object: EMMessageListenerAdapter(){

        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            super.onMessageReceived(p0)
            mPresenter.loadConversation()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_conversation

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.message)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ConversationAdapter(context, mPresenter.conversations)
        }

        EMClient.getInstance().chatManager().addMessageListener(mEMMgsListener)
    }

    override fun onStartLoadConversation() {
       showProgressDlg(getString(R.string.loading_msg))
    }

    override fun onLoadConversationSuccess() {
        hideProgressDlg()
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onLoadConversationFail() {
        hideProgressDlg()
        toast(R.string.load_msg_fail)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.loadConversation()
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(mEMMgsListener)
    }
}