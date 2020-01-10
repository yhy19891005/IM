package com.yhy.im.presenter

import com.hyphenate.chat.EMClient
import com.yhy.im.adapter.EMCallBackAdapter
import com.yhy.im.contract.LoginContract
import com.yhy.im.extentions.isValidPsw
import com.yhy.im.extentions.isValidUsername

class LoginPresenter(val view: LoginContract.View): LoginContract.Presenter {

    override fun login(userName: String, psw: String) {
        if(userName.isValidUsername()){
            if(psw.isValidPsw()){
                view.onStartLogin()
                loginEaseMob(userName,psw)
            }else{
                view.onPswError()
            }
        }else{
            view.onUserNameError()
        }
    }

    private fun loginEaseMob(userName: String, psw: String){
        EMClient.getInstance().login(userName,psw,object: EMCallBackAdapter() {
            //环信SDK特点:登录成功或者失败的回调是在子线程中进行
            override fun onSuccess() {

                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
                //在主线程中通知视图层
                onUIThread {
                    view.onLoginSuccess()
                }
            }

            override fun onError(p0: Int, msg: String?) {
                //在主线程中通知视图层
                onUIThread {
                    view.onLoginFail(msg)
                }
            }
        })
    }
}