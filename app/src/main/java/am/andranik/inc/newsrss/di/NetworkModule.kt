package am.andranik.inc.newsrss.di

import am.andranik.inc.newsrss.BuildConfig
import am.andranik.inc.newsrss.data.network.api.NewsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single { provideOkHttpClient(get()) }
    single { provideNewsApi(get()) }
}

private fun provideNewsApi(okHttpClient: OkHttpClient): NewsApi {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_NEWS)
        .addConverterFactory(
            SimpleXmlConverterFactory.createNonStrict(
                Persister(
                    AnnotationStrategy()
                )
            )
        )
        .client(okHttpClient)
        .build()
        .create(NewsApi::class.java)
}

private fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addNetworkInterceptor(loggingInterceptor)
        .build()
}
