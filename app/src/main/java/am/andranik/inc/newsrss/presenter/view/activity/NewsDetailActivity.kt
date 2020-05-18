package am.andranik.inc.newsrss.presenter.view.activity

import am.andranik.inc.newsrss.BuildConfig
import am.andranik.inc.newsrss.R
import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.presenter.viewmodel.ArchiveViewModel
import am.andranik.inc.newsrss.util.NetworkHelper
import am.andranik.inc.newsrss.util.showMessage
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_news_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val PARAM_KEY_NEWS = "param.key.news";
        const val PARAM_KEY_NEWS_LINK = "param.key.news.link";
    }

    private val archiveViewModel: ArchiveViewModel by viewModel()
    private var news: NewsUI? = null
    private var link: String? = null
    private var htmlContent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        initToolBar()

        initNewsObserver()

        displayNews()
    }

    private fun displayNews() {
        if (intent != null) {
            if (intent.hasExtra(PARAM_KEY_NEWS)) {
                news = intent.getSerializableExtra(PARAM_KEY_NEWS) as NewsUI
                if (NetworkHelper.isNetworkAvailable(this)) {
                    news?.link?.let { initWebView(it) }
                } else {
                    news?.link?.let { archiveViewModel.loadArchive(it) }
                }
            } else if (intent.hasExtra(PARAM_KEY_NEWS_LINK)) {
                link = intent.getStringExtra(PARAM_KEY_NEWS_LINK)
                if (NetworkHelper.isNetworkAvailable(this)) {
                    link?.let { initWebView(it) }
                } else {
                    link?.let { archiveViewModel.loadArchive(it) }
                }
            }
        }
    }

    private fun initToolBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
    }

    private fun initNewsObserver() {
        archiveViewModel.singleNews.observe(this, Observer { newsUI ->
            news = newsUI
            newsUI.html?.let { initWebView(it) } ?: run { initWebView(newsUI.link) }
        })
        archiveViewModel.message.observe(this, Observer { message ->
            root_view_news_detail.showMessage(message)
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(link: String) {
        web_view.settings.javaScriptEnabled = true
        web_view.addJavascriptInterface(MyJavaScriptInterface(), "HtmlViewer");

        content_loading_progress_bar.visibility = View.VISIBLE
        content_loading_progress_bar.show()
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                content_loading_progress_bar.hide()
                if (news?.html == null && NetworkHelper.isNetworkAvailable(this@NewsDetailActivity)
                        .not()
                ) {
                    root_view_news_detail.showMessage(R.string.unable_ope_web_page)
                }
                super.onReceivedError(view, request, error)
            }

            override fun onPageFinished(view: WebView, url: String) {
                content_loading_progress_bar.hide()
                if (url == link) {
                    web_view.loadUrl(
                        "javascript:window.HtmlViewer.loadHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"
                    )
                } else {
                    super.onPageFinished(view, url)
                }
            }
        }

        if (link == news?.link || link == this.link) {
            web_view.loadUrl(link)
        } else {
            web_view.loadDataWithBaseURL(BuildConfig.BASE_URL_NEWS, link, "text/html", "UTF-8", "");
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_news_detail, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_save_content)?.isEnabled = NetworkHelper.isNetworkAvailable(this)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_save_content -> {
                archiveContent()
                true
            }
            R.id.menu_open_in_web -> {
                openInChrome()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openInChrome() {
        news?.let {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
            startActivity(browserIntent)
        }
    }

    private fun archiveContent() {
        htmlContent?.let { html ->
            news?.let {
                it.html = html
                archiveViewModel.saveNews(it)
                root_view_news_detail.showMessage(R.string.saved_in_archive)
            }
        } ?: run {
            root_view_news_detail.showMessage(R.string.not_ready_to_archive)
        }
    }

    inner class MyJavaScriptInterface() {

        @JavascriptInterface
        fun loadHTML(html: String?) {
            htmlContent = html
        }

    }

}