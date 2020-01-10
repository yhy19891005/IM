package com.yhy.im.data.db

import android.annotation.SuppressLint
import com.yhy.im.extentions.toVarargArray
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class IMDataBase {

    companion object{
        @SuppressLint("StaticFieldLeak")
        val mHelper = DataBaseHelper()
        val instance = IMDataBase()
    }

    //保存联系人到数据库
    fun saveContact(contact: Contact){
        mHelper.use {
            //*表示将数组转成可变参数
            insert(ContactTable.TABLE_NAME, *contact.map.toVarargArray())
        }
    }

    //查询所有联系人
    fun getAllContacts(): List<Contact> =
        mHelper.use {
            select(ContactTable.TABLE_NAME).parseList(object: MapRowParser<Contact>{
                override fun parseRow(columns: Map<String, Any?>): Contact =
                    Contact(columns.toMutableMap())
            })
        }

    //删除所有联系人
    fun deleteAllContacts(){
        mHelper.use {
            delete(ContactTable.TABLE_NAME,null,null)
        }
    }
}