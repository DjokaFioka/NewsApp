package rs.djokafioka.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rs.djokafioka.news.data.util.Resource
import rs.djokafioka.news.databinding.FragmentNewsBinding
import rs.djokafioka.news.presentation.adapter.NewsAdapter
import rs.djokafioka.news.presentation.viewmodel.NewsViewModel

class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var fragmentNewsBinding: FragmentNewsBinding
    private var country = "us"
    private var page = 1
    private var isScrolling = false
    private var isLoading = false
    private var isLastPage = false
    private var pages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNewsBinding = FragmentNewsBinding.bind(view)
        viewModel = (activity as MainActivity).getViewModel()
        newsAdapter = (activity as MainActivity).newsAdapter
        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("selected_article", article) //We have to declare the argument in the same way in the nav_graph.xml for this destination
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_infoFragment,
                bundle
            )
        }
        initRecyclerView()
        viewNewsList()
        setSearchView()
    }

    private fun initRecyclerView() {
        //newsAdapter = NewsAdapter() //removed this part of code with di AdapterModule and injecting it in the MainActivity
        fragmentNewsBinding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.onScrollListener) //we overrided onScrollListener with manual pagination
        }
    }

    private fun viewNewsList() {
        viewModel.getNewsHeadlines(country, page)
        viewModel.newsHeadlines.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        Log.d("NewsTAG", "came here ${it.articles.toList().size}")
                        newsAdapter.differ.submitList(it.articles.toList())
                        pages = if (it.totalResults % 20 == 0) {
                            it.totalResults / 20 //20 is default page size of the API
                        } else {
                            it.totalResults / 20 + 1
                        }

                        isLastPage = page == pages
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occured: $it", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showProgressBar() {
        Log.d("NewsTAG", "showProgressBar: ")
        isLoading = true
        fragmentNewsBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        Log.d("NewsTAG", "hideProgressBar: ")
        isLoading = false
        fragmentNewsBinding.progressBar.visibility = View.INVISIBLE
    }

    //Implementing manual paging
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = fragmentNewsBinding.rvNews.layoutManager as LinearLayoutManager
            val sizeOfTheCurrentList = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val topPosition = layoutManager.findFirstVisibleItemPosition()

            val hasReachedTheEnd = topPosition + visibleItems >= sizeOfTheCurrentList
            val shouldPaginate = !isLoading && !isLastPage && hasReachedTheEnd && isScrolling
            if (shouldPaginate) {
                page++
                viewModel.getNewsHeadlines(country, page)
                isScrolling = false
                Log.d("NewsTAG", "New Page -> $page")
            }
        }
    }

    //Search
    private fun setSearchView() {
        fragmentNewsBinding.svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchNews(country, query.toString(), page)
                viewSearchedNews()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                MainScope().launch {
                    delay(2000L)
                    viewModel.searchNews(country, newText.toString(), page)
                    viewSearchedNews()
                }
                return false
            }
        })

        fragmentNewsBinding.svNews.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                initRecyclerView()
                viewNewsList()
                return false
            }
        })
    }

    fun viewSearchedNews() {
        viewModel.searchedNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        Log.d("NewsTAG", "came here ${it.articles.toList().size}")
                        newsAdapter.differ.submitList(it.articles.toList())
                        pages = if (it.totalResults % 20 == 0) {
                            it.totalResults / 20 //20 is default page size of the API
                        } else {
                            it.totalResults / 20 + 1
                        }

                        isLastPage = page == pages
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "An error occured: $it", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

}