package com.yhy.im.ui.fragment

import com.hyphenate.chat.EMClient
import com.yhy.im.R
import com.yhy.im.adapter.EMCallBackAdapter
import com.yhy.im.base.BaseFragment
import com.yhy.im.ui.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_dynamic.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

//动态Fragment
class DynamicFragment: BaseFragment(){

    override fun getLayoutResId(): Int = R.layout.fragment_dynamic

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.dynamic)
        val logoutStr = String.format(getString(R.string.logout),EMClient.getInstance().currentUser)
        logout.text = logoutStr
        logout.onClick {
            if(isAdded && activity != null && !activity.isFinishing){
                showProgressDlg(getString(R.string.logouting))
            }
            logout() //退出登录
        }
    }

    fun logout(){
        EMClient.getInstance().logout(true,object: EMCallBackAdapter(){

            override fun onSuccess() {
                super.onSuccess()
                activity.runOnUiThread{
                    hideProgressDlg()
                    activity.toast(R.string.logout_success)
                }
                activity.startActivity<LoginActivity>()
                activity.finish()
            }

            override fun onError(p0: Int, p1: String?) {
                super.onError(p0, p1)
                activity.runOnUiThread{
                    hideProgressDlg()
                    activity.toast(R.string.logout_failed)
                }
            }
        })
    }

}