package com.example.bestvideoloader

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestvideoloader.adapters.VideoListAdapter
import com.example.bestvideoloader.viewmodels.ListVideosViewModel

class ListVideosFragment : Fragment(R.layout.fragment_list_videos) {

    private lateinit var videoListAdapter: VideoListAdapter
    private lateinit var viewModel: ListVideosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this
        ).get(ListVideosViewModel::class.java)
        setUpResultListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEnterNumberButtonClickListener()
        initView()
        initObservers()
    }

    private fun initView() {
        videoListAdapter = VideoListAdapter(requireContext())

        val rv = requireView().findViewById<RecyclerView>(R.id.list_videos)
        rv.apply {
            adapter = videoListAdapter
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        }
    }

    private fun initObservers() {
        viewModel.loadedVideosUri.observe(viewLifecycleOwner) {
            videoListAdapter.setData(it)
        }
    }

    private fun setUpEnterNumberButtonClickListener() {
        val loadNewVideo = requireView().findViewById<Button>(R.id.load_new_video)
        loadNewVideo.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AddVideoFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setUpResultListener() {
        parentFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
    }

    @SuppressLint("RestrictedApi")
    private fun onFragmentResult(requestKey: String, result: Bundle) {
        Preconditions.checkState(REQUEST_KEY == requestKey)
        val uri = result.get(KEY_NUMBER) as? Uri
        if (uri != null) {
            viewModel.addVideo(uri)
        }
    }

    companion object {
        const val REQUEST_KEY = "result-listener-request-key"
        const val KEY_NUMBER = "key-number"
    }
}