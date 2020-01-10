package com.yhy.im.ui.activity

import android.support.v7.widget.LinearLayoutManager
import com.yhy.im.R
import com.yhy.im.adapter.AddContactAdapter
import com.yhy.im.base.BaseActivity
import com.yhy.im.contract.AddContactsContract
import com.yhy.im.presenter.AddFriendPresenter
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

class AddFriendActivity: BaseActivity(),AddContactsContract.View {

    val mPresenter by lazy{ AddFriendPresenter(this)}

    override fun getLayoutResId(): Int = R.layout.activity_add_friend

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.add_friend)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = AddContactAdapter(context,mPresenter.addContactList)
        }

        search.setOnClickListener {
            searchContacts()
        }

        userName.setOnEditorActionListener{
                textView, i, keyEvent ->
                searchContacts()
                true
        }

    }

    private fun searchContacts() {
        hideSoftKeyboard()
        showProgressDlg(getString(R.string.searching))
        val keyword = userName.text.toString().trim()
        mPresenter.searchContacts(keyword)
    }

    override fun onSearchContactsSuccess() {
        hideProgressDlg()
        toast(R.string.search_success)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onSearchContactsFail() {
        hideProgressDlg()
        toast(R.string.search_failed)
    }
}