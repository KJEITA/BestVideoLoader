package com.example.bestvideoloader.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListVideosViewModel : ViewModel() {
    private val _loadedVideosUri: MutableLiveData<ArrayList<Uri>> = MutableLiveData(arrayListOf())
    val loadedVideosUri: LiveData<ArrayList<Uri>>
        get() = _loadedVideosUri


    fun addVideo(uri: Uri){
       val oldArr = _loadedVideosUri.value
        oldArr?.add(uri)
        _loadedVideosUri.value = oldArr
    }
}