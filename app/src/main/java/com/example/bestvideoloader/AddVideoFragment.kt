package com.example.bestvideoloader

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.bestvideoloader.models.VKVideoFileUploadInfo
import com.example.bestvideoloader.requests.VKVideoPostCommand
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class AddVideoFragment : Fragment(R.layout.fragment_add_video) {

    private var contentURI: Uri? = null
    private lateinit var progressBar: ProgressBar

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        progressBar = requireView()?.findViewById(R.id.progressBar)

        val btnOpenGallery = requireView()?.findViewById<Button>(R.id.open_gallery)
        btnOpenGallery?.setOnClickListener {
            chooseVideoFromGallery()
        }

        val btnBack = requireView()?.findViewById<Button>(R.id.back)
        btnBack?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val btnLoad = requireView()?.findViewById<Button>(R.id.load_new_video)
        btnLoad?.setOnClickListener {
            contentURI?.let { sendMyVideo(it) }
        }
    }

    private fun sendMyVideo(uri: Uri) {
        VK.execute(
            VKVideoPostCommand(uri, ::setUpProgress),
            object : VKApiCallback<VKVideoFileUploadInfo> {
                override fun fail(error: Exception) {
                    Toast.makeText(requireContext(), "fail", Toast.LENGTH_SHORT).show()
                }

                override fun success(result: VKVideoFileUploadInfo) {
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                    setUpProgress(100, 100)
                    setResult()
                }
            })
    }

    private fun setUpProgress(value: Int, maxValue: Int) {
        progressBar.max = maxValue
        progressBar.progress = value
    }

    private fun chooseVideoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            data?.data?.let {
                contentURI = it
                setUpVideoView()
            }
        }
    }

    private fun setUpVideoView() {
        val videoView = requireView().findViewById<VideoView>(R.id.vv) ?: return
        videoView.setVideoURI(contentURI)
        videoView.requestFocus()
        videoView.start()
    }


    private fun setResult() {
        parentFragmentManager.setFragmentResult(
            ListVideosFragment.REQUEST_KEY,
            bundleOf(ListVideosFragment.KEY_NUMBER to contentURI)
        )
    }
}