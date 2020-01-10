package com.yhy.im.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.yhy.im.base.IMAplication
import org.jetbrains.anko.db.*

class DataBaseHelper(val context: Context = IMAplication.context ): ManagedSQLiteOpenHelper(context, NAME, null, VERSION) {

    companion object{
        val NAME = "im.db"
        val VERSION = 1
    }

    //数据库创建时调用
    override fun onCreate(db: SQLiteDatabase?) {
        /*
        * 参数一:表名
        * 参数二:为true表示表名不存在的时候就创建该表
        * 参数三:表示将id设为主键,并自动递增
        * 参数四:表示将username设计为键名,并且值为TEXT型
        * */
        db?.createTable(ContactTable.TABLE_NAME, true,
            ContactTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            ContactTable.USER_NAME to TEXT)
    }

    //数据库升级时调用
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //如果contact表存在,就删除
        db?.dropTable(ContactTable.TABLE_NAME,true)
        //再重新创建
        onCreate(db)
    }
}