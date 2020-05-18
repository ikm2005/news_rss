package am.andranik.inc.newsrss.data.network.api

import am.andranik.inc.newsrss.data.network.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApi {
    @GET("{language}/rss/")
    suspend fun getNews(@Path(value = "language") language: String): NewsResponseDto
}