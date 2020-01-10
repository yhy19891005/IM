package com.yhy.im.data.db

data class Contact(val map: MutableMap<String,Any?>) {

    //将变量 _id, name全都委托给map
    val _id by map
    val name by map
}