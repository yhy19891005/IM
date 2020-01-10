package com.yhy.im.presenter

import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.yhy.im.contract.RegistContract
import com.yhy.im.extentions.isValidPsw
import com.yhy.im.extentions.isValidUsername
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RegistPresenter(val view: RegistContract.View): RegistContract.Presenter {

    override fun regist(userName: String, psw: String, confirmPsw: String) {
        if(userName.isValidUsername()){
            if(psw.isValidPsw()){
               if(confirmPsw.equals(psw)){
                   view.onStartRegist()
                   registToBomb(userName,psw)
               }else{
                   view.onConfirmPswError()
               }
            }else{
                view.onPswError()
            }
        }else{
            view.onUserNameError()
        }
    }

    private fun registToBomb(userName: String, psw: String) {
        val bmobUser = BmobUser()
        bmobUser.username = userName
        bmobUser.setPassword(psw)
        bmobUser.signUp<BmobUser>(object : SaveListener<BmobUser>(){
            override fun done(user: BmobUser?, e: BmobException?) {
                if(null == e){
                    registEaseMob(userName,psw)
                }else{
                    if(e.errorCode == 202){ //Bmob官方文档 错误码202即为用户已存在
                        view.onUserExist()
                    } else{
                        view.onRegistFail()
                    }
                }
            }
        })
    }

    private fun registEaseMob(userName: String, psw: String) {
        doAsync {
            try {
                EMClient.getInstance().createAccount(userName, psw)//同步方法
                uiThread { //主线程回调,anko库提供的方法
                    view.onRegistSuccess()
                }
            } catch (e: HyphenateException) {
                uiThread {
                    view.onRegistFail()
                }
            }
        }

    }
}