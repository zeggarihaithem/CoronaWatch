package solutus.coronawatch.ui.mainActivity.home.novelties.listArticles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import solutus.coronawatch.data.reposetory.implementation.ArticlesRepository

class NoveltiesViewModelFactory(private val articleRepository: ArticlesRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoveltiesViewModel(
            articleRepository
        ) as T
    }

}