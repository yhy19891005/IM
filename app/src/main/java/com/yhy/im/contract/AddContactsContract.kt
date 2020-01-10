package com.yhy.im.contract

interface AddContactsContract {

    interface Presenter: BasePresenter{
        fun searchContacts(keyword: String)
    }

    interface View{
        fun onSearchContactsSuccess()  //搜索联系人成功时调用
        fun onSearchContactsFail()     //搜索联系人失败时调用
    }
}