package com.yhy.im.contract

interface RegistContract {

    interface Presenter: BasePresenter{
        fun regist(userName: String, psw: String, confirmPsw: String)
    }

    interface View{
        fun onUserNameError()     //用户名不合法时调用
        fun onPswError()          //密码错误时调用
        fun onConfirmPswError()   //确认密码错误时调用
        fun onStartRegist()       //开始注册时调用,例如显示进度条
        fun onRegistSuccess()     //注册成功时调用
        fun onRegistFail()        //注册失败时调用
        fun onUserExist()         //用户名已存在时调用
    }
}