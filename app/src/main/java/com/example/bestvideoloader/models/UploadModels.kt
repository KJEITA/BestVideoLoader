package com.example.bestvideoloader.models

class VKVideoFileUploadInfo(
    val size: Int,
    val videoId: Int
)

class VKVideoUploadInfo(
    val uploadUrl: String,
    val videoId: Int,
    val title: String,
    val description: String,
    val ownerId: Int
)

