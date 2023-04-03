package solutus.coronawatch.ui.mainActivity.home.novelties.listArticles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.novelties_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import solutus.coronawatch.data.entity.Article
import solutus.coronawatch.data.entity.WriterPost
import solutus.coronawatch.ui.mainActivity.home.novelties.adapter.ArticleAdapter
import solutus.coronawatch.utilities.InjectorUtils

class NoveltiesFragment : Fragment()   , KodeinAware{

    override val kodein: Kodein by closestKodein()
    companion object {
        fun newInstance() =
            NoveltiesFragment()
    }

    private  val viewModel: NoveltiesViewModel by instance<NoveltiesViewModel>()
    private lateinit var adapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.novelties_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //set ViewModel
        initializeUi()
    }

    private fun initializeUi() {
        //set recycle view adapter
        val recyclerView: RecyclerView = list_article as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = ArticleAdapter(requireContext())
        recyclerView.adapter = adapter

        try {
            viewModel.getArticles()
            viewModel.articles.observe(viewLifecycleOwner , Observer {
                Log.d("Debug" , "Update the articleList")
                if (it.isEmpty()){
                    nouvelties_progress.visibility = View.VISIBLE
                }
                else{
                    nouvelties_progress.visibility = View.GONE
                }
                adapter.setArticles(it)
            })

        } catch (e : Exception){
            println("Debug ${e.message}")
            e.printStackTrace()
        }

    }
}

