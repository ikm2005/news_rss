package am.andranik.inc.newsrss.presenter.view.fragment

import am.andranik.inc.newsrss.R
import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.presenter.view.activity.NewsDetailActivity
import am.andranik.inc.newsrss.presenter.view.adapter.NewsAdapter
import am.andranik.inc.newsrss.presenter.viewmodel.ArchiveViewModel
import am.andranik.inc.newsrss.util.showMessage
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_archive.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArchiveFragment : Fragment() {

    private val archiveViewModel: ArchiveViewModel by viewModel()
    private lateinit var newsAdapter: NewsAdapter

    private val listener: NewsAdapter.OnItemClickListener = object :
        NewsAdapter.OnItemClickListener {
        override fun onItemClicked(newsUI: NewsUI) {
            startActivity(Intent(context, NewsDetailActivity::class.java).apply {
                putExtra(NewsDetailActivity.PARAM_KEY_NEWS_LINK, newsUI.link)
            })
        }

        override fun onItemRemoveClicked(link: String, position: Int) {
            archiveViewModel.removeArchive(link)
            newsAdapter.removeItem(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        archiveViewModel.loadArchives()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_archive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        rv_archive.layoutManager = LinearLayoutManager(activity)
        newsAdapter = NewsAdapter(mutableListOf(), NewsAdapter.NewsType.ARCHIVE, listener)
        rv_archive.adapter = newsAdapter

        archiveViewModel.news.observe(viewLifecycleOwner, Observer { newsList ->
            newsAdapter.updateData(newsList)
        })
        archiveViewModel.message.observe(viewLifecycleOwner, Observer { message ->
            showMessage(message)
        })
    }
}
