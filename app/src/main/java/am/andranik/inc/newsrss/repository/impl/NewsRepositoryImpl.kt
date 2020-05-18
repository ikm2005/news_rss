package am.andranik.inc.newsrss.repository.impl

import am.andranik.inc.newsrss.data.converters.DateConverter
import am.andranik.inc.newsrss.data.converters.NewsConverter
import am.andranik.inc.newsrss.data.network.api.NewsApi
import am.andranik.inc.newsrss.data.util.SharedPreferencesHelper
import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.repository.NewsRepository
import am.andranik.inc.newsrss.repository.util.Resource
import am.andranik.inc.newsrss.repository.util.ResponseHandler

class NewsRepositoryImpl(
    private val newsApi: NewsApi,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val newsConverter: NewsConverter,
    private val dateConverter: DateConverter,
    private val responseHandler: ResponseHandler
) : NewsRepository {

    override suspend fun getNewsList(): Resource<List<NewsUI>> {
        return try {
            val newsLanguage = sharedPreferencesHelper.getNewsLanguage()
            val newsResponse = newsApi.getNews(newsLanguage)
            newsResponse.channelDto?.items?.let { it ->
                val resultList = it.map { newsDto ->
                    newsConverter.dtoToModel(
                        newsDto,
                        newsLanguage,
                        sharedPreferencesHelper.getLastNewsDate()
                    )
                }
                if (resultList.isNotEmpty()) {
                    sharedPreferencesHelper.setLastNewsDate(resultList[0].date)
                }
                responseHandler.handleSuccess(resultList)
            } ?: run {
                return Resource.error("Something went wrong, Please try later", null)
            }
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun checkUpdates(): Boolean {
        return try {
            var hasUpdate = false
            val newsLanguage = sharedPreferencesHelper.getNewsLanguage()
            val newsResponse = newsApi.getNews(newsLanguage)
            newsResponse.channelDto?.items?.let { it ->
                if (it.isNotEmpty()) {
                    val lastNewsDate = sharedPreferencesHelper.getLastNewsDate()
                    val entityNewDate = dateConverter.stringDateToLong(it[0].pubDate)
                    hasUpdate = lastNewsDate != -1L && entityNewDate > lastNewsDate
                }
            }
            hasUpdate
        } catch (e: Exception) {
            false
        }
    }
}