@file:Suppress("DEPRECATION")

package com.yhy.im.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.yhy.im.R

@Suppress("DEPRECATION")
abstract class BaseActivity: AppCompatActivity(){

    val mProgressDialog by lazy { ProgressDialog(this) }
    val mInputMothedManager by lazy{ getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        init()
    }

    //初始化操作,子类可以复写完成自己的初始化
    open fun init(){

    }

    //展示进度圈
    fun showProgressDlg(msg: String){
        mProgressDialog.setMessage(msg)
        mProgressDialog.setProgressDrawable(resources.getDrawable(R.drawable.load_msg_progress))
        mProgressDialog.show()
    }

    //隐藏进度圈
    fun hideProgressDlg(){
        if(mProgressDialog.isShowing){
            mProgressDialog.dismiss()
        }
    }

    //隐藏软键盘
    fun hideSoftKeyboard(){
        mInputMothedManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
    }

    //获取Activity对应的布局资源Id
    abstract fun getLayoutResId(): Int
}