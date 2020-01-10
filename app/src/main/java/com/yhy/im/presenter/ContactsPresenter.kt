package com.yhy.im.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.yhy.im.contract.ContactsContract
import com.yhy.im.data.ContactInfo
import com.yhy.im.data.db.Contact
import com.yhy.im.data.db.IMDataBase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ContactsPresenter(val view: ContactsContract.View) : ContactsContract.Presenter {

    val contactsList = mutableListOf<ContactInfo>()

    override fun loadContacts() {

        doAsync {
            try {
                //获取所有联系人名集合
                val contacts = EMClient.getInstance().contactManager().allContactsFromServer
                //按首字母排序
                contacts.sortBy {
                    it[0]
                }
                //清空联系人数据库
                IMDataBase.instance.deleteAllContacts()
                //先清空数据集,避免重复添加
                contactsList.clear()
                //将联系人信息封装成bean类,并放入集合中
                contacts.forEachIndexed{
                    index, username ->
                    //1.第一个条目显示首字母
                    //2.前后两个条目首字母不一样时,显示首字母
                    val showFirstLetter = index == 0 || username[0] != contacts[index-1][0]
                    val info = ContactInfo(username,username[0],showFirstLetter)
                    contactsList.add(info)
                    //将联系人保存到数据库
                    IMDataBase.instance.saveContact(Contact(mutableMapOf("name" to username)))
                }

                if(contactsList.size > 0){
                    uiThread {
                        //主线程回调,anko库提供的方法
                        view.onloadContactsSuccess()
                    }
                }else {
                    uiThread {
                        view.onloadContactsFail(true)
                    }
                }

            } catch (e: HyphenateException) {
                uiThread {
                    view.onloadContactsFail(false)
                }
            }
        }
    }

}