package solutus.coronawatch.ui.mainActivity.home.novelties.listArticles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import solutus.coronawatch.data.entity.Article
import solutus.coronawatch.data.entity.WriterPost
import solutus.coronawatch.data.reposetory.implementation.ArticlesRepository
import solutus.coronawatch.utilities.Coroutines
import java.lang.Exception

class NoveltiesViewModel(
    private val articleRepository: ArticlesRepository
) : ViewModel() {

    init {
        articleRepository.articles.observeForever {
            _articles.postValue(it)
            println("The data are on the ViewModel's LiveData")
        }
    }

    private lateinit var job: Job
    private val _articles = MutableLiveData<List<Article>>()

    val articles: LiveData<List<Article>>
        get() = _articles

    //to change later
    fun getArticles()  {
        try {
            CoroutineScope(IO).launch {
                articleRepository.getArticles()
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
    override fun onCleared() {
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}
