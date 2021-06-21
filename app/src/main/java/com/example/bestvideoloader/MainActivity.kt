package com.example.bestvideoloader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.bestvideoloader.models.VKVideoFileUploadInfo
import com.example.bestvideoloader.requests.VKVideoPostCommand
import com.vk.api.sdk.*
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import okhttp3.*


class MainActivity : AppCompatActivity() {
    private lateinit var progress: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        VK.login(this, arrayListOf(VKScope.VIDEO))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                initView()
            }

            override fun onLoginFailed(errorCode: Int) {
            }
        }

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback))
            super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val contentURI: Uri? = data?.data
            val videoView = findViewById<VideoView>(R.id.vv)
            videoView.setVideoURI(contentURI)
            videoView.requestFocus()
            videoView.start()

            if (contentURI != null) {
                sendMyVideo(contentURI)
            }
        }
    }

    private fun initView() {
        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            chooseVideoFromGallery()
        }
        progress = findViewById(R.id.simple_progress)
    }

    private fun chooseVideoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 1)
    }

    private fun sendMyVideo(uri: Uri) {
        VK.execute(
            VKVideoPostCommand(uri, ::setUpProgress),
            object : VKApiCallback<VKVideoFileUploadInfo> {
                override fun fail(error: Exception) {
                    Toast.makeText(this@MainActivity, "fail", Toast.LENGTH_SHORT).show()
                }

                override fun success(result: VKVideoFileUploadInfo) {
                    Toast.makeText(this@MainActivity, "success", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setUpProgress(value: Int, maxValue: Int) {
        progress.max = maxValue
        progress.progress = value
    }
}

