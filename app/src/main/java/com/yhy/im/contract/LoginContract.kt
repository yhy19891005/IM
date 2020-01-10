package com.yhy.im.contract

interface LoginContract {

    interface Presenter: BasePresenter{
        fun login(userName: String, psw: String)
    }

    interface View{
        fun onUserNameError() //用户名不合法时调用
        fun onPswError()      //密码错误时调用
        fun onStartLogin()    //开始登录时调用,例如显示进度条
        fun onLoginSuccess()  //登录成功时调用
        fun onLoginFail(msg: String?)     //登录失败时调用
    }
}