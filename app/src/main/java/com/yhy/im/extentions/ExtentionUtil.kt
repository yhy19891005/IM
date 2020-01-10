package com.yhy.im.extentions

//Kotlin特色:扩展函数
//用户名: 字母开头,3到20位
fun String.isValidUsername(): Boolean = this.matches(Regex("^[a-zA-Z]\\w{2,19}$"))
//密码: 8到20位数字
fun String.isValidPsw(): Boolean = this.matches(Regex("^[0-9]{8,20}$"))
// 将MutableMap转换成Pair数组
fun<K,V> MutableMap<K,V>.toVarargArray(): Array<Pair<K,V>> =
    map{
        Pair(it.key,it.value)
    }.toTypedArray()