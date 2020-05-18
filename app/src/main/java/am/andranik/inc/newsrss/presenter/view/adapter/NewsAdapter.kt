package am.andranik.inc.newsrss.presenter.view.adapter

import am.andranik.inc.newsrss.R
import am.andranik.inc.newsrss.models.NewsUI
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(
    private val items: MutableList<NewsUI>,
    private val newsType: NewsType,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_news,
            parent,
            false
        )
        view.img_remove.isVisible = newsType == NewsType.ARCHIVE
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        (holder).bindNewsItem(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(position: Int): NewsUI = items[position]

    fun updateData(items: List<NewsUI>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnItemClickListener {
        fun onItemClicked(newsUI: NewsUI)

        fun onItemRemoveClicked(link: String, position: Int)
    }

    inner class NewsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindNewsItem(position: Int) {
            val newsItem = getItem(position)

            view.tv_date.text = newsItem.formattedDate
            view.tv_title.text = newsItem.title
            Glide.with(view.context)
                .load(newsItem.imageUrl)
                .centerCrop()
                .error(R.drawable.ic_image_placeholder)
                .into(view.iv_cover)

            view.setOnClickListener { listener.onItemClicked(newsItem) }
            view.img_remove.setOnClickListener {
                listener.onItemRemoveClicked(
                    newsItem.link,
                    position
                )
            }

            if (newsItem.isNew) {
                view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.new_item_background
                    )
                )
            } else {
                view.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.white))
            }
        }
    }

    enum class NewsType {
        FEED,
        ARCHIVE
    }

}