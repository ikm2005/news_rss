package am.andranik.inc.newsrss.presenter.view.fragment

import am.andranik.inc.newsrss.R
import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.presenter.view.activity.NewsDetailActivity
import am.andranik.inc.newsrss.presenter.view.adapter.NewsAdapter
import am.andranik.inc.newsrss.presenter.viewmodel.NewsViewModel
import am.andranik.inc.newsrss.util.showMessage
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment() {

    private val newsViewModel: NewsViewModel by viewModel()
    private lateinit var newsAdapter: NewsAdapter

    private val listener: NewsAdapter.OnItemClickListener = object :
        NewsAdapter.OnItemClickListener {
        override fun onItemClicked(newsUI: NewsUI) {
            startActivity(Intent(context, NewsDetailActivity::class.java).apply {
                putExtra(NewsDetailActivity.PARAM_KEY_NEWS, newsUI)
            })
        }

        override fun onItemRemoveClicked(link: String, position: Int) {
            // Don't need implementation for news Feed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        swipe_container.setOnRefreshListener {
            newsViewModel.loadNews()
        }

        rv_news.layoutManager = LinearLayoutManager(activity)
        newsAdapter = NewsAdapter(mutableListOf(), NewsAdapter.NewsType.FEED, listener)
        rv_news.adapter = newsAdapter

        newsViewModel.news.observe(viewLifecycleOwner, Observer { newsList ->
            swipe_container.isRefreshing = false
            newsAdapter.updateData(newsList)
        })
        newsViewModel.message.observe(viewLifecycleOwner, Observer { message ->
            swipe_container.isRefreshing = false
            showMessage(message)
        })
        newsViewModel.loading.observe(viewLifecycleOwner, Observer {loading ->
            if (loading) {
                content_loading_progress_bar_news.visibility = View.VISIBLE
                content_loading_progress_bar_news.show()
            } else {
                content_loading_progress_bar_news.hide()
            }
        })
    }
}
