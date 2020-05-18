package am.andranik.inc.newsrss.data.converters

import am.andranik.inc.newsrss.BuildConfig
import am.andranik.inc.newsrss.data.db.entity.NewsEntity
import am.andranik.inc.newsrss.data.network.dto.NewsDto
import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.util.empty

class NewsConverter(private val dateConverter: DateConverter) {

    private val imageUrlStructure: String =
        "${BuildConfig.BASE_URL_NEWS}img/news/%s/%s/%s/default.jpg"

    fun dtoToModel(dtoObject: NewsDto, newsLanguage: String, lastNewsDate: Long): NewsUI {
        val pairFormattedDate = dateConverter.stringDateToLongAndFormatted(dtoObject.pubDate)
        return NewsUI(
            title = dtoObject.title.orEmpty(),
            link = dtoObject.link.orEmpty(),
            imageUrl = convertLinkToImageUrl(dtoObject.link),
            date = pairFormattedDate.first,
            formattedDate = pairFormattedDate.second,
            newsLanguage = newsLanguage,
            isNew = lastNewsDate > 0 && (lastNewsDate < pairFormattedDate.first)
        )
    }

    fun uiToDao(ui: NewsUI): NewsEntity {
        return NewsEntity(
            title = ui.title,
            link = ui.link,
            date = ui.date,
            formattedDate = ui.formattedDate,
            imageUrl = ui.imageUrl,
            newsLanguage = ui.newsLanguage,
            html = ui.html
        )
    }

    fun daoToUI(daoEntity: NewsEntity, convertHtml: Boolean = false): NewsUI {
        return NewsUI(
            title = daoEntity.title,
            link = daoEntity.link,
            date = daoEntity.date,
            formattedDate = daoEntity.formattedDate,
            imageUrl = daoEntity.imageUrl,
            newsLanguage = daoEntity.newsLanguage,
            html = if (convertHtml) daoEntity.html else null
        )
    }

    private fun convertLinkToImageUrl(link: String?): String {
        return try {
            if (link.isNullOrEmpty()) {
                String.empty()
            } else {
                val id = link.substring(link.lastIndexOf('/') + 1, link.lastIndexOf("."))
                val first = id.substring(0, 2)
                val second = id.substring(2, 4)
                val thirds = id.substring(4)

                String.format(imageUrlStructure, first, second, thirds)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            String.empty()
        }
    }
}