package com.example.bestvideoloader

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKSaveVideoRequest: VKRequest<VKVideoSaveDAO> {
    constructor(): super("video.save") {
        addParam("name", "SimpleName")
        addParam("description", "SimpleName")
        addParam("wallpost", 0)
        addParam("privacy_view", "nobody / only_me")
        addParam("repeat", 1)

    }

    override fun parse(r: JSONObject): VKVideoSaveDAO {
        val response = r.getJSONObject("response")
        return VKVideoSaveDAO.parse(response)
    }
}