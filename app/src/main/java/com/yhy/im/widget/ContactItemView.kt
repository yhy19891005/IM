package com.yhy.im.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.yhy.im.R
import com.yhy.im.data.ContactInfo
import kotlinx.android.synthetic.main.view_contact_item.view.*

class ContactItemView: RelativeLayout {

    constructor(context: Context?) : this(context,null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
       View.inflate(context, R.layout.view_contact_item,this)
    }

    fun bindData(info: ContactInfo) {
        firstLetter.text = info.firstLetter.toString()
        userName.text = info.contactname
        if(info.showFirstLetter){
            firstLetter.visibility = View.VISIBLE
        }else {
            firstLetter.visibility = View.GONE
        }
    }
}