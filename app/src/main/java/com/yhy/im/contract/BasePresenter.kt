package com.yhy.im.contract

import android.os.Handler
import android.os.Looper

interface BasePresenter {

    companion object{
        val mHandler by lazy {
            Handler(Looper.getMainLooper())
        }
    }

    fun onUIThread(f: () -> Unit){
        mHandler.post { f() }
    }
}