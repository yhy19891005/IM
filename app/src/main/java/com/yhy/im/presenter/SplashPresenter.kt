package com.yhy.im.presenter

import com.hyphenate.chat.EMClient
import com.yhy.im.contract.SplashContract

class SplashPresenter(val view: SplashContract.View): SplashContract.Presenter {

    override fun checkLoginStatus() {
        if(isLogin()) view.onLogin() else view.notLogin()
    }

    private fun isLogin(): Boolean =
            EMClient.getInstance().isConnected && EMClient.getInstance().isLoggedInBefore
}