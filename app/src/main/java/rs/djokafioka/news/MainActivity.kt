package rs.djokafioka.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.djokafioka.news.databinding.ActivityMainBinding
import rs.djokafioka.news.presentation.adapter.NewsAdapter
import rs.djokafioka.news.presentation.viewmodel.NewsViewModel
import rs.djokafioka.news.presentation.viewmodel.NewsViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: NewsViewModelFactory //can't be private because Dagger does not support injection into private fields
    @Inject
    lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvNews.setupWithNavController(navController)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    }

    fun getViewModel() = viewModel
}