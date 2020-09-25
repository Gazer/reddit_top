package com.example.reddittop.models


data class Children(
    val `data`: DataX,
    val kind: String
) {
    // This can be improved, just checking for some edge cases I found
    fun getThumbnail(): String? = when {
        data.thumbnail.isBlank() -> null
        data.thumbnail == "self" -> null
        data.media != null -> data.thumbnail
        !data.url.endsWith(".jpg") && !data.url.endsWith(".png") -> data.thumbnail
        else -> data.url
    }
}