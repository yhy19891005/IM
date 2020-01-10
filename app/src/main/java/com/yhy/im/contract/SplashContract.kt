package com.yhy.im.contract

interface SplashContract {

    interface Presenter: BasePresenter{
        fun checkLoginStatus() //检查登录状态
    }

    interface View{
        fun notLogin() //未登录
        fun onLogin()  //已登录
    }
}