package com.example.myapplicationkotlin

class AccessData(val title: String, val content: String, val time: String, val lockImage: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccessData

        if (title != other.title) return false
        if (content != other.content) return false
        if (time != other.time) return false
        if (lockImage != other.lockImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + lockImage
        return result
    }

    override fun toString(): String {
        return "AccessData(txt1='$title', text2='$content', text3='$time', imageId=$lockImage)"
    }

}
