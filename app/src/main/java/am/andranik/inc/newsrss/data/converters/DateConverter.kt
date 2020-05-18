package am.andranik.inc.newsrss.data.converters

import am.andranik.inc.newsrss.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    private val parser = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.getDefault())
    private val formatter = SimpleDateFormat("HH:mm, dd-MM-yyyy", Locale.getDefault())
    private val emptyPair = Pair(0L, "")

    fun stringDateToLongAndFormatted(stringDate: String?): Pair<Long, String> {
        return if (stringDate.isNullOrEmpty()) {
            emptyPair
        } else {
            return try {
                val date: Date? = parser.parse(stringDate)
                date?.let {
                    val formattedDate = formatter.format(it)
                    Pair(date.time, formattedDate)
                } ?: emptyPair
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
                emptyPair
            }
        }
    }

    fun stringDateToLong(stringDate: String?): Long {
        return stringDate?.let {
            val date: Date? = parser.parse(stringDate)
            date?.time ?: Long.MIN_VALUE
        } ?: run { Long.MIN_VALUE }
    }
}