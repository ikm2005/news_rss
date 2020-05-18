package am.andranik.inc.newsrss.data.db.dao

import am.andranik.inc.newsrss.data.db.entity.NewsEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER BY date DESC")
    suspend fun getAll(): List<NewsEntity>

    @Query("SELECT * FROM news WHERE news_language = :newsLanguage ORDER BY date DESC")
    suspend fun getAllByLanguage(newsLanguage: String): List<NewsEntity>

    @Query("SELECT * FROM news WHERE link = :link")
    suspend fun getByLink(link: String): NewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: NewsEntity): Long

    @Query("DELETE FROM news WHERE link = :link")
    suspend fun deleteByLink(link: String): Int

    @Query("DELETE FROM news")
    suspend fun clear(): Int
}