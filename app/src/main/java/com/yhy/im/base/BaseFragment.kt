package com.yhy.im.base

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yhy.im.R

abstract class BaseFragment: Fragment() {

    val mProgressDialog by lazy { ProgressDialog(activity) }

    //方法里面只有一行代码时,可以去掉{},用=连接
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(getLayoutResId(),null)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    //初始化操作,子类可以复写完成自己的初始化
    open fun init(){

    }

    //展示进度圈
    fun showProgressDlg(msg: String){
        mProgressDialog.setMessage(msg)
        mProgressDialog.setProgressDrawable(resources.getDrawable(R.drawable.load_msg_progress))
        if(!activity.isFinishing){
            mProgressDialog.show()
        }
    }

    //隐藏进度圈
    fun hideProgressDlg(){
        if(mProgressDialog.isShowing){
            mProgressDialog.dismiss()
        }
    }

    //获取Fragment对应的布局资源Id
    abstract fun getLayoutResId(): Int
}