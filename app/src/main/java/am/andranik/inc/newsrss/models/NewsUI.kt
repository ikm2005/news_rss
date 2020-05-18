package am.andranik.inc.newsrss.models

import java.io.Serializable

data class NewsUI(
    val title: String,
    val link: String,
    val date: Long,
    val formattedDate: String,
    val imageUrl: String,
    val newsLanguage: String,
    val isNew: Boolean = false,
    var html: String? = null
) : Serializable