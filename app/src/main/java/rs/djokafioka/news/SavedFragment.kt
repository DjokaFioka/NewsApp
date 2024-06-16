package rs.djokafioka.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rs.djokafioka.news.databinding.FragmentSavedBinding
import rs.djokafioka.news.presentation.adapter.NewsAdapter
import rs.djokafioka.news.presentation.viewmodel.NewsViewModel

class SavedFragment : Fragment() {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedBinding.bind(view)
        viewModel = (activity as MainActivity).getViewModel()
        newsAdapter = (activity as MainActivity).newsAdapter
        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("selected_article", article) //We have to declare the argument in the same way in the nav_graph.xml for this destination
            }
            findNavController().navigate(
                R.id.action_savedFragment_to_infoFragment,
                bundle
            )
        }
        initRecyclerView()
        viewModel.getSavedNews().observe(viewLifecycleOwner) {
            newsAdapter.differ.submitList(it)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
//                Snackbar.make(view, "News article deleted successfully", Snackbar.LENGTH_LONG)
//                    .setAction("Undo") {
//                        viewModel.saveArticle(article)
//                    }
//                    .show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSaved)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.snackBarDataFlow.collectLatest { data ->
                    Snackbar.make(
                        binding.root,
                        resources.getString(data.resId),
                        Snackbar.LENGTH_LONG
                    ).setAction(R.string.undo) {
                        viewModel.restoreArticle(data.data)
                    }.show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.snackBarSaveFlow.collectLatest { text ->
                    Snackbar.make(
                        binding.root,
                        text,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private fun initRecyclerView() {
        binding.rvSaved.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}