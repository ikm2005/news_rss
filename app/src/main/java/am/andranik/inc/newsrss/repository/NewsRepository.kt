package am.andranik.inc.newsrss.repository

import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.repository.util.Resource

interface NewsRepository {
    suspend fun getNewsList(): Resource<List<NewsUI>>

    suspend fun checkUpdates(): Boolean
}