package com.yhy.im.presenter

import android.util.Log
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.hyphenate.chat.EMClient
import com.yhy.im.contract.AddContactsContract
import com.yhy.im.data.AddContactInfo
import com.yhy.im.data.db.IMDataBase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddFriendPresenter(val view: AddContactsContract.View): AddContactsContract.Presenter {

    val addContactList = mutableListOf<AddContactInfo>()

    override fun searchContacts(keyword: String) {
        val query = BmobQuery<BmobUser>()
        //模糊搜索,需付费才能达到预期效果
        query.addWhereContains("username",keyword)
        //不能搜索到自己
        query.addWhereNotEqualTo("username",EMClient.getInstance().currentUser)
        query.findObjects(object: FindListener<BmobUser>() {
            //主线程调用
            override fun done(list: MutableList<BmobUser>?, be: BmobException?) {
               if(be == null){
                   //获取所有联系人信息
                   val allContacts = IMDataBase.instance.getAllContacts()
                   Log.e("dddddd","allContacts.size = ${allContacts.size}")
                   doAsync {
                       list?.let {
                           it.forEach {
                               //比对搜索出来的联系人是否已添加
                               var isAdd = false
                               for(c in allContacts){
                                   if(c.name == it.username){
                                       isAdd = true
                                   }
                               }
                               val info = AddContactInfo(it.username,it.createdAt,isAdd)
                               addContactList.add(info)
                           }
                       }

                       uiThread {
                           view.onSearchContactsSuccess()
                       }
                   }

               }else {
                   view.onSearchContactsFail()
               }
            }

        })
    }
}