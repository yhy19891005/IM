package com.yhy.im.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hyphenate.chat.EMClient
import com.yhy.im.R
import com.yhy.im.adapter.ContactAdapter
import com.yhy.im.adapter.EMContactListenerAdapter
import com.yhy.im.base.BaseFragment
import com.yhy.im.contract.ContactsContract
import com.yhy.im.presenter.ContactsPresenter
import com.yhy.im.ui.activity.AddFriendActivity
import com.yhy.im.widget.SlideBar
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

//联系人Fragment
class ContactsFragment: BaseFragment(),ContactsContract.View{

    val mPresenter by lazy { ContactsPresenter(this) }

    override fun getLayoutResId(): Int = R.layout.fragment_contacts

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.contact)
        add.visibility = View.VISIBLE

        add.setOnClickListener {
           context.startActivity<AddFriendActivity>()
        }

        swipeRefreshLayout.apply {
           setColorSchemeResources(R.color.qq_blue)
            isRefreshing = true
            setOnRefreshListener{
                mPresenter.loadContacts()
            }
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ContactAdapter(context,mPresenter.contactsList)
        }

        //加载联系人数据
        mPresenter.loadContacts()

        EMClient.getInstance().contactManager().setContactListener(object: EMContactListenerAdapter(){

            override fun onContactAdded(p0: String?) {
                //重新加载联系人数据
                mPresenter.loadContacts()
            }

            override fun onContactDeleted(p0: String?) {
                //重新加载联系人数据
                mPresenter.loadContacts()
            }
        })

        slideBar.mListener = object: SlideBar.OnSectionChangeListener{
            override fun onSectionChange(touchLetter: String) {
                //显示首字母
                section.visibility = View.VISIBLE
                section.text = touchLetter
            }

            override fun onSectionFinish() {
                section.visibility = View.GONE
            }

        }
    }

    override fun onloadContactsSuccess() {
        if(swipeRefreshLayout != null && isAdded){
            swipeRefreshLayout.isRefreshing = false
            recyclerView.adapter.notifyDataSetChanged()
        }
    }

    override fun onloadContactsFail(noData: Boolean) {
        if(swipeRefreshLayout != null && isAdded){
            swipeRefreshLayout.isRefreshing = false
            if(noData){
                context.toast(R.string.no_contacts_data)
            }else{
                context.toast(R.string.load_contacts_failed)
            }
        }
    }

}