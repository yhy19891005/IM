package com.yhy.im.ui.activity

import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.yhy.im.R
import com.yhy.im.adapter.EMMessageListenerAdapter
import com.yhy.im.base.BaseActivity
import com.yhy.im.utils.FragmentFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    val mEMMsgListener = object: EMMessageListenerAdapter(){
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            super.onMessageReceived(p0)
            runOnUiThread{
                updateUnreadMsgCount()
            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun init() {
        super.init()
        bottomBar.setOnTabSelectListener {
            tabId ->
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = FragmentFactory.INSTENSE.getFragment(tabId)
            fragment?.let {
                transaction.replace(R.id.fragment_frame,it)
                transaction.commit()
            }
        }
        EMClient.getInstance().chatManager().addMessageListener(mEMMsgListener)

        EMClient.getInstance().addConnectionListener(object: EMConnectionListener{
            override fun onConnected() {

            }

            override fun onDisconnected(p0: Int) {
                //账号在其他设备登录
               if(p0 == EMError.USER_LOGIN_ANOTHER_DEVICE){
                   startActivity<LoginActivity>()
                   runOnUiThread{
                       toast(R.string.user_login_another_device)
                   }
                   finish()
               }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        updateUnreadMsgCount()
    }

    private fun updateUnreadMsgCount(){
        val msgTab = bottomBar?.getTabWithId(R.id.tab_conversation)
        msgTab?.setBadgeCount(EMClient.getInstance().chatManager().unreadMessageCount)
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(mEMMsgListener)
    }
}
