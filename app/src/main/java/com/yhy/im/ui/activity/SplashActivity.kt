package com.yhy.im.ui.activity

import android.os.Handler
import com.yhy.im.R
import com.yhy.im.base.BaseActivity
import com.yhy.im.contract.SplashContract
import com.yhy.im.presenter.SplashPresenter
import org.jetbrains.anko.startActivity

class SplashActivity: BaseActivity(),SplashContract.View {

    val mHandler by lazy { Handler() }
    val mPresenter by lazy { SplashPresenter(this) }

    companion object{
       val DELAY = 2000L
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun init() {
        super.init()
        mPresenter.checkLoginStatus()
    }

    override fun notLogin() {
        mHandler.postDelayed({
            startActivity<LoginActivity>()
            finish()
        }, DELAY)
    }

    override fun onLogin() {
        mHandler.postDelayed({
            startActivity<MainActivity>()
            finish()
        }, DELAY)
    }
}