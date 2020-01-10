package com.yhy.im.contract

interface ContactsContract {

    interface Presenter: BasePresenter{
        fun loadContacts()
    }

    interface View{
        fun onloadContactsSuccess()  //加载联系人成功时调用
        fun onloadContactsFail(noData: Boolean)     //加载联系人失败时调用 noData为true代表该用户没有添加过联系人
    }
}