package am.andranik.inc.newsrss.repository.util

import am.andranik.inc.newsrss.BuildConfig
import retrofit2.HttpException
import java.net.UnknownHostException

open class ResponseHandler {
    fun <T : Any?> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any?> handleException(e: Exception): Resource<T> {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
        return when (e) {
            is HttpException -> Resource.error(
                getErrorMessage(e.code(), message = e.message()),
                null
            )
            is UnknownHostException -> Resource.error(
                "No network available",
                null
            )
            else -> Resource.error(getErrorMessage(Int.MAX_VALUE, message = e.message), null)
        }
    }

    private fun getErrorMessage(code: Int, message: String?): String {
        return when (code) {
            404 -> "Page not available, Please try later"
            else -> message ?: "Something went wrong, Please try later"
        }
    }
}