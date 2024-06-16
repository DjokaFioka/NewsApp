package rs.djokafioka.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rs.djokafioka.news.databinding.FragmentInfoBinding
import rs.djokafioka.news.presentation.viewmodel.NewsViewModel

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInfoBinding.bind(view)
        val args: InfoFragmentArgs by navArgs()
        val article = args.selectedArticle

        viewModel = (activity as MainActivity).getViewModel()

        binding.wvInfo.apply {
            webViewClient = WebViewClient()
            if (article.url != null && article.url != "") {
                loadUrl(article.url)
            }
        }
        binding.fabSave.setOnClickListener {
            viewModel.saveArticle(article)
            //Snackbar.make(view, "News article saved successfully", Snackbar.LENGTH_LONG).show()
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
}