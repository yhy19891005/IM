package com.yhy.im.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.yhy.im.R
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.sp

class SlideBar: View {

    companion object{
        val LETTERS = arrayOf("A","B","C","D","E","F","G","H","I","J","K","L","M",
                              "N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
    }

    val mPaint = Paint()
    var letterH = 0F
    var baseLine = 0F
    var mListener: OnSectionChangeListener? = null

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
       mPaint.apply {
           //画笔颜色
           color = resources.getColor(R.color.qq_section_text_color)
           //文字居中对齐
           textAlign = Paint.Align.CENTER
           //文字大小,anko库提供的方法
           textSize = sp(12).toFloat()
       }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        letterH = height * 1.0F/ LETTERS.size
        val metrics = mPaint.fontMetrics
        //descent为正值,ascent为负值
        val textH = metrics.descent - metrics.ascent
        //文字基线
        baseLine = letterH/2 - (textH/2 - metrics.descent)
    }

    override fun onDraw(canvas: Canvas) {
        val letterW = width * 1.0F / 2
        LETTERS.forEachIndexed{
            index,str ->
            canvas.drawText(str, letterW, baseLine + letterH*index, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                backgroundResource = R.drawable.bg_slide_bar
                mPaint.color = resources.getColor(R.color.white)
                invalidate()
                mListener?.onSectionChange(getTouchLetter(event))

            }
            MotionEvent.ACTION_UP -> {
                backgroundColor = Color.TRANSPARENT
                mPaint.color = resources.getColor(R.color.qq_section_text_color)
                invalidate()
                mListener?.onSectionFinish()
            }
            MotionEvent.ACTION_MOVE -> {
                mListener?.onSectionChange(getTouchLetter(event))
            }
        }
        return true //消费触摸事件
    }

    private fun getTouchLetter(event: MotionEvent): String {
        val y = event.y
        var index = (y / letterH).toInt()
        if(index < 0){
            index = 0
        }else if(index >= LETTERS.size){
            index = LETTERS.size - 1
        }
        return LETTERS[index]
    }

    interface OnSectionChangeListener{

        //点击或者滑动时显示首字母
        fun onSectionChange(touchLetter: String)
        //抬起手指,隐藏首字母
        fun onSectionFinish()
    }
}