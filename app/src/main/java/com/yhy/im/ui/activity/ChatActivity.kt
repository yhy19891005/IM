package com.yhy.im.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.View
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.yhy.im.R
import com.yhy.im.adapter.EMMessageListenerAdapter
import com.yhy.im.adapter.MsgListAdapter
import com.yhy.im.adapter.TextWatcherAdapter
import com.yhy.im.base.BaseActivity
import com.yhy.im.contract.ChatContract
import com.yhy.im.presenter.ChatPresenter
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ChatActivity: BaseActivity(), ChatContract.View{

    val mPresenter by lazy{ ChatPresenter(this)}
    lateinit var username: String

    val mEMMessageListener = object: EMMessageListenerAdapter(){

        //子线程回调
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            mPresenter.addMsg(username, p0)
            //切换至主线程刷新UI
            runOnUiThread {
                recyclerView.adapter.notifyDataSetChanged()
            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_chat

    override fun init() {
        super.init()
        username = intent.getStringExtra("name")
        val title = String.format(getString(R.string.chat_title),username)
        headerTitle.text = title

        back.visibility = View.VISIBLE
        back.onClick {
            finish()
        }

        edit.addTextChangedListener(object: TextWatcherAdapter(){
            override fun afterTextChanged(s: Editable?) {
                //输入消息后,发送按钮变为可点击状态
                send.isEnabled = !s.isNullOrEmpty()
            }
        })

        //键盘上的搜索按钮点击监听
        edit.setOnEditorActionListener{
            v, actionId, event ->
            sendMsg()
            true
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MsgListAdapter(context,mPresenter.msgList)
            //RecyclerView滑动加载更多
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    //终止滑动时
                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        //判断第一个可见条目是否为第一条数据
                        val manager =  layoutManager as LinearLayoutManager
                        if(manager.findFirstVisibleItemPosition() == 0){
                            mPresenter.loadMoreMsg(username)
                        }
                    }
                }
            })
        }

        send.onClick {
            sendMsg()
        }

        //监听收到消息
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener)

        //初始化聊天记录
        mPresenter.loadMsg(username)
    }

    private fun sendMsg(){
        val msg = edit.text.toString().trim()
        mPresenter.sendMsg(msg,username)
        recyclerView.scrollToPosition(recyclerView.adapter.itemCount - 1)
    }

    override fun onSendMsgBegin() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onSendMsgSuccess() {
        recyclerView.adapter.notifyDataSetChanged()
        edit.text.clear()
    }

    override fun onSendMsgFail() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onAfterLoadMsg() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener)
    }
}