package com.yhy.im.base

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import cn.bmob.v3.Bmob
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMOptions
import com.hyphenate.chat.EMTextMessageBody
import com.yhy.im.BuildConfig
import com.yhy.im.R
import com.yhy.im.adapter.EMMessageListenerAdapter
import com.yhy.im.ui.activity.ChatActivity

class IMAplication: Application() {

    companion object{
        //延迟初始化
        lateinit var context: IMAplication
    }

    val soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)

    val DUAN by lazy { soundPool.load(context, R.raw.duan, 0) }
    val CHANG by lazy { soundPool.load(context, R.raw.yulu, 0) }
    
    //收到消息后播放声音
    val mEMMsgListener = object: EMMessageListenerAdapter(){
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            super.onMessageReceived(p0)
            if(isForeground()){
                soundPool.play(DUAN,1F,1F,0,0,1F)
            }else {
                soundPool.play(CHANG,1F,1F,0,0,1F)
                showNotification(p0)
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        val options = EMOptions()
// 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false)
        options.setAcceptInvitationAlways(true)
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true)
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true)
//初始化环信
        EMClient.getInstance().init(applicationContext, options)
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG)
//初始化Bmob
        Bmob.initialize(this, "a9c1896e8c9aa5e60eda943a13bdc63f")

        EMClient.getInstance().chatManager().addMessageListener(mEMMsgListener)
    }

    //判断APP是否运行在前台
    private fun isForeground(): Boolean{
        //获取ActivityManager
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //通过ActivityManager获取正在运行的APP进程,并进行遍历
        for(progress in manager.runningAppProcesses){
            //通过包名获取本APP(包名就是进程名)
            if(progress.processName == packageName){
                //判断手否在前台
               return progress.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }

    private fun showNotification(p0: MutableList<EMMessage>?){
        p0?.forEach{
            var text = getString(R.string.no_text_message)
            if(it.type == EMMessage.Type.TXT){
                text = (it.body as EMTextMessageBody).message
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val bitmap = BitmapFactory.decodeResource(resources,R.mipmap.avatar1)
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name",it.conversationId())
            //val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            //防止出现点击通知进入聊天页,再点击返回按钮造成的退出APP的错觉
            val taskStackBuilder =TaskStackBuilder.create(context)
                                                  .addParentStack(ChatActivity::class.java)
                                                  .addNextIntent(intent)
            val pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            val notification = Notification.Builder(context)
                .setContentTitle(getString(R.string.receive_new_message))
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_contact)
                .setLargeIcon(bitmap)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification.setChannelId(packageName) //必须添加（Android 8.0） 【唯一标识】
                //9.0及以上系统显示通知必须设置NotificationChannel
                val channel = NotificationChannel(packageName,"会话消息(IM)",NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)
            }
            manager.notify(0,notification.build())
        }
    }
}