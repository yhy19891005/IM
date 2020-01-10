package com.yhy.im.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.hyphenate.chat.EMClient
import com.yhy.im.R
import com.yhy.im.adapter.EMCallBackAdapter
import com.yhy.im.data.AddContactInfo
import kotlinx.android.synthetic.main.view_add_friend_item.view.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

class AddContactItemView: RelativeLayout {

    constructor(context: Context?) : this(context,null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        View.inflate(context, R.layout.view_add_friend_item, this)
    }

    fun bindData(info: AddContactInfo) {
        userName.text = info.contactname
        timestamp.text = info.registTime
        if(info.isAdded){
            add.isEnabled = false
            add.text = context.getString(R.string.already_added)
        }else{
            add.isEnabled = true
            add.text = context.getString(R.string.add)
        }

        add.setOnClickListener {
            addFriend(info.contactname)
        }
    }

    private fun addFriend(contactname: String) {
        EMClient.getInstance()
                .contactManager()
                .aysncAddContact(contactname,"",object: EMCallBackAdapter(){

                    override fun onSuccess() {
                        context.runOnUiThread {
                            context.toast(R.string.send_add_friend_success)
                        }
                    }

                    override fun onError(p0: Int, p1: String?) {
                        context.runOnUiThread {
                            context.toast(R.string.send_add_friend_failed)
                        }
                    }
                })
    }
}