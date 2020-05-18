package am.andranik.inc.newsrss.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "news", indices = [Index(value = ["link"], unique = true)])
data class NewsEntity(
    val title: String,
    @PrimaryKey
    val link: String,
    val date: Long,
    @ColumnInfo(name = "formatted_date")
    val formattedDate: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "news_language")
    val newsLanguage: String,
    val html: String?
)