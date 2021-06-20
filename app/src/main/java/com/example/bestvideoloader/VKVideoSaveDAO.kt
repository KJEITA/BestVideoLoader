package com.example.bestvideoloader

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKVideoSaveDAO(
    val access_key: String = "",
    val description: String = "",
    val owner_id: Int = 0,
    val title: String = "",
    val upload_url: String = "",
    val video_id: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(access_key)
        parcel.writeString(description)
        parcel.writeInt(owner_id)
        parcel.writeString(title)
        parcel.writeString(upload_url)
        parcel.writeInt(video_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VKVideoSaveDAO> {
        override fun createFromParcel(parcel: Parcel): VKVideoSaveDAO {
            return VKVideoSaveDAO(parcel)
        }

        override fun newArray(size: Int): Array<VKVideoSaveDAO?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject) = VKVideoSaveDAO(
            access_key = json.optString("access_key", ""),
            description = json.optString("description", ""),
            owner_id = json.optInt("owner_id", 0),
            title = json.optString("title", ""),
            upload_url = json.optString("upload_url", ""),
            video_id = json.optInt("video_id", 0)
        )
    }
}