package solutus.coronawatch.ui.mainActivity.home.novelties.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch_mobile.R
import com.squareup.picasso.Picasso
import solutus.coronawatch.data.entity.Article
import solutus.coronawatch.data.entity.WriterPost
import solutus.coronawatch.service.Browser
import solutus.coronawatch.service.ChromeClient


class ArticleAdapter(val context: Context) : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {
    private var articles: List<Article> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)
        return ArticleHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: ArticleHolder, position: Int) {
        val currentArticle: Article = articles[position]
        //set article title
        holder.articleTitleView.text = currentArticle.title
        //set article content
       // setArticleContent(holder.articleContent, currentArticle.url,holder.progressBar)
        setWebViewContent(holder.articleContent , currentArticle.url , holder.progressBar)
        //set redactor name
        val publisher = "${currentArticle.publisher.firstName} ${currentArticle.publisher.lastName}"
        holder.redactorName.text = publisher
        //set user avatar
       Picasso.get().load(currentArticle.publisher.image).into(holder.redactorAvatar)

    }

    private fun setWebViewContent(webView: WebView , content : String ,progressBar: ProgressBar){
         val mimeType = "text/html";
         val  encoding = "UTF-8";
        progressBar.visibility = View.GONE
         webView.loadDataWithBaseURL("",content , mimeType , encoding , "")
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(articles: List<Article>) {
        println("Debug : $articles")
        this.articles = articles
        notifyDataSetChanged()
    }


    inner class ArticleHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal val articleTitleView: TextView = itemView.findViewById(R.id.article_title)
        internal val articleContent: WebView = itemView.findViewById(R.id.article_view)
        internal val redactorName: TextView = itemView.findViewById(R.id.redactor_name)
        internal val redactorAvatar: de.hdodenhof.circleimageview.CircleImageView =
            itemView.findViewById(R.id.redactor_avatar)
        internal val progressBar : ProgressBar = itemView.findViewById(R.id.progress_bar)


    }


}