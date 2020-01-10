package com.yhy.im.ui.activity

import com.yhy.im.R
import com.yhy.im.base.BaseActivity
import com.yhy.im.contract.RegistContract
import com.yhy.im.presenter.RegistPresenter
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class RegistActivity: BaseActivity(), RegistContract.View{

    val mPresenter by lazy { RegistPresenter(this) }

    override fun getLayoutResId(): Int = R.layout.activity_register

    override fun init() {
        super.init()
        register.onClick {
            regist()
        }

        confirmPassword.setOnEditorActionListener{
            p0,p1,p2 ->
            regist()
            true
        }
    }

    fun regist(){
        hideSoftKeyboard()
        mPresenter.regist(userName.text.toString(),password.text.toString(),confirmPassword.text.toString())
    }

    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    override fun onPswError() {
        password.error = getString(R.string.password_error)
    }

    override fun onConfirmPswError() {
        confirmPassword.error = getString(R.string.confirm_password_error)
    }

    override fun onStartRegist() {
        showProgressDlg(getString(R.string.registering))
    }

    override fun onRegistSuccess() {
        hideProgressDlg()
        toast(R.string.register_success)
        finish()
    }

    override fun onRegistFail() {
        hideProgressDlg()
        toast(R.string.register_failed)
    }

    override fun onUserExist() {
        hideProgressDlg()
        toast(R.string.user_already_exist)
    }
}