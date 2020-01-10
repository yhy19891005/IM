package com.yhy.im.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import com.yhy.im.R
import com.yhy.im.base.BaseActivity
import com.yhy.im.contract.LoginContract
import com.yhy.im.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity: BaseActivity(),LoginContract.View {

    companion object{
        val REQUEST_PERMISSION_CODE = 100
        val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val mPresenter by lazy { LoginPresenter(this) }

    override fun getLayoutResId(): Int = R.layout.activity_login

    override fun init() {
        super.init()

        //登录按钮点击事件
        login.onClick {
            login()
        }

        //新用户按钮点击事件
        newUser.onClick {
            startActivity<RegistActivity>()
        }

    }

    private fun login() {
        //隐藏软键盘
        hideSoftKeyboard()
        //环信会创建并维护一个数据库来存储用户信息,需要读写磁盘权限
        if(hasPermission()){
            //登录
            mPresenter.login(userName.text.toString(), password.text.toString())
        }else {
            applyPermission()
        }

    }

    private fun hasPermission(): Boolean {
        for(i in 0 until PERMISSIONS.size){
            val result = ActivityCompat.checkSelfPermission(this, PERMISSIONS[i])
            if(result != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    private fun applyPermission() {
        ActivityCompat.requestPermissions(this,
            PERMISSIONS,
            REQUEST_PERMISSION_CODE
        )
    }

    //用户名不合法时调用
    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    //密码错误时调用
    override fun onPswError() {
        password.error = getString(R.string.password_error)
    }

    //开始登录时调用,例如显示进度条
    override fun onStartLogin() {
        showProgressDlg(getString(R.string.logging))
    }

    //登录成功时调用
    override fun onLoginSuccess() {
        hideProgressDlg()
        startActivity<MainActivity>()
        finish()
    }

    //登录失败时调用
    override fun onLoginFail(msg: String?) {
        hideProgressDlg()
        msg?.let {
            toast(msg)
        }
        if(TextUtils.isEmpty(msg)){
            toast(getString(R.string.login_failed))
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var result = false
        if(requestCode == REQUEST_PERMISSION_CODE){
            for(i in 0 until grantResults.size){
                result = grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }

        if(result){
            login()
        }else{
            toast(R.string.permission_denied)
        }
    }

}