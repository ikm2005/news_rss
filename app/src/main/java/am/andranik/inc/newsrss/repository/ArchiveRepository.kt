package am.andranik.inc.newsrss.repository

import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.repository.util.Resource

interface ArchiveRepository {
    suspend fun getArchives(): Resource<List<NewsUI>>

    suspend fun removeArchive(link: String): Resource<Boolean>

    suspend fun saveArchive(newsUI: NewsUI): Resource<Boolean>

    suspend fun getArchive(link: String): Resource<NewsUI>
}