package com.example.bestvideoloader

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VK.login(this, arrayListOf(VKScope.VIDEO))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                sendMyVideo()
            }

            override fun onLoginFailed(errorCode: Int) {
                // User didn't pass authorization
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

//    fun sample(){
//        val fields = listOf(UsersFields.SEX, UsersFields.LAST_NAME_ABL)
//        VK.execute(FriendsService().friendsGet(fields = fields), object: VKApiCallback<FriendsGetFieldsResponse> {
//            override fun success(result: FriendsGetFieldsResponse) {
//                // you stuff is here
//                result.items.forEach {
//                    Log.d("MyFriends", it.toString())
//                }
//            }
//            override fun fail(error: Exception) {
//                Log.e("TAG", error.toString())
//            }
//        })
//    }

    fun sendMyVideo(){
        VK.execute(VKSaveVideoRequest(), object: VKApiCallback<VKVideoSaveDAO> {
            override fun success(result: VKVideoSaveDAO) {
                val i = 5+5
            }

            override fun fail(error: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

}