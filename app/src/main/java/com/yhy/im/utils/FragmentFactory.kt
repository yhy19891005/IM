package com.yhy.im.utils

import com.yhy.im.R
import com.yhy.im.base.BaseFragment
import com.yhy.im.ui.fragment.ContactsFragment
import com.yhy.im.ui.fragment.ConversationFragment
import com.yhy.im.ui.fragment.DynamicFragment

class FragmentFactory private constructor(){

    companion object{
        val INSTENSE = FragmentFactory()
        val mConversationFragment = ConversationFragment()
        val mContactsFragment = ContactsFragment()
        val mDynamicFragment = DynamicFragment()
    }

    fun getFragment(tabId: Int): BaseFragment?{
        when(tabId){
            R.id.tab_conversation -> return mConversationFragment
            R.id.tab_contacts -> return mContactsFragment
            R.id.tab_dynamic -> return mDynamicFragment
        }
        return null
    }
}