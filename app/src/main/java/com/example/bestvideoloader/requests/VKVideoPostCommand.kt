package com.example.bestvideoloader.requests

import android.net.Uri
import com.example.bestvideoloader.models.*
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class VKVideoPostCommand(private val videoUri: Uri) : ApiCommand<VKVideoFileUploadInfo>() {
    override fun onExecute(manager: VKApiManager): VKVideoFileUploadInfo {
        val uploadInfo = getVideoUploadInfo(manager)

        val fileUploadCall = VKHttpPostCall.Builder()
            .url(uploadInfo.uploadUrl)
            .args("video_file", videoUri, "video.mp4")
            .timeout(TimeUnit.MINUTES.toMillis(5))
            .retryCount(RETRY_COUNT)
            .build()

        return manager.execute(
            fileUploadCall, null,
            FileUploadInfoParser()
        )
    }

    companion object {
        const val RETRY_COUNT = 3
    }

    private fun getVideoUploadInfo(manager: VKApiManager): VKVideoUploadInfo {
        val uploadInfoCall = VKMethodCall.Builder()
            .method("video.save")
            .args("name", "superVideo")
            .args("description", "myFirstVideo")
            .args("wallpost", 0)
            .args("privacy_view", "nobody / only_me")
            .version(manager.config.version)
            .build()
        return manager.execute(uploadInfoCall, VideoUploadInfoParser())
    }
}

private class VideoUploadInfoParser : VKApiResponseParser<VKVideoUploadInfo> {
    override fun parse(response: String): VKVideoUploadInfo {
        try {
            val joResponse = JSONObject(response).getJSONObject("response")
            return VKVideoUploadInfo(
                uploadUrl = joResponse.getString("upload_url"),
                videoId = joResponse.getInt("video_id"),
                title = joResponse.getString("title"),
                description = joResponse.getString("description"),
                ownerId = joResponse.getInt("owner_id")
            )
        } catch (ex: JSONException) {
            throw VKApiIllegalResponseException(ex)
        }
    }
}

private class FileUploadInfoParser : VKApiResponseParser<VKVideoFileUploadInfo> {
    override fun parse(response: String): VKVideoFileUploadInfo {
        try {
            val joResponse = JSONObject(response)
            return VKVideoFileUploadInfo(
                size = joResponse.getInt("size"),
                videoId = joResponse.getInt("video_id"),
            )
        } catch (ex: JSONException) {
            throw VKApiIllegalResponseException(ex)
        }
    }
}