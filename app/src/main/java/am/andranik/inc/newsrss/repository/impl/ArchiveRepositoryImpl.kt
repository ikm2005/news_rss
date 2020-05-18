package am.andranik.inc.newsrss.repository.impl

import am.andranik.inc.newsrss.data.converters.NewsConverter
import am.andranik.inc.newsrss.data.db.dao.NewsDao
import am.andranik.inc.newsrss.data.util.SharedPreferencesHelper
import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.repository.ArchiveRepository
import am.andranik.inc.newsrss.repository.util.Resource

class ArchiveRepositoryImpl(
    private val newsDao: NewsDao,
    private val newsConverter: NewsConverter,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ArchiveRepository {

    override suspend fun getArchives(): Resource<List<NewsUI>> {
        val newsLanguage = sharedPreferencesHelper.getNewsLanguage()
        val entityList = newsDao.getAllByLanguage(newsLanguage)
        val news = entityList.map { newsEntity -> newsConverter.daoToUI(newsEntity) }
        return Resource.success(news)
    }

    override suspend fun removeArchive(link: String): Resource<Boolean> {
        val response = newsDao.deleteByLink(link)
        return if (response > 0) {
            Resource.success(true)
        } else {
            Resource.error("Something went wrong", false)
        }
    }

    override suspend fun saveArchive(newsUI: NewsUI): Resource<Boolean> {
        val newsEntity = newsConverter.uiToDao(newsUI)
        val id = newsDao.insert(newsEntity)
        return if (id > 0) {
            Resource.success(true)
        } else {
            Resource.error("Something went wrong", false)
        }
    }

    override suspend fun getArchive(link: String): Resource<NewsUI> {
        val newsEntity = newsDao.getByLink(link)
        return if (newsEntity != null) {
            val newsUI = newsConverter.daoToUI(newsEntity, true)
            Resource.success(newsUI)
        } else {
            Resource.error("News doesn't exist in archive", null)
        }
    }
}