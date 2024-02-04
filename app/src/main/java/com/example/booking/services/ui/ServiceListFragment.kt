package com.example.booking.services.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.booking.MainActivity
import com.example.booking.R
import com.example.booking.common.utils.hideKeyboard
import com.example.booking.databinding.FragmentServiceListBinding
import com.example.booking.services.ui.viewmodel.ServiceListViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServiceListFragment : Fragment() {
    private lateinit var binding: FragmentServiceListBinding

    private lateinit var pagerAdapter: ServiceListPagerAdapter

    private val viewModel: ServiceListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServiceListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopBar()
        setupTab()
        subscribeToViewModel()
    }

    private fun setupTopBar() {
        with(binding) {
            searchBar.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch()
                    hideKeyboard()
                }
                true
            }
            ivFilters.setOnClickListener {
                showFilters()
            }
            ivProfile.setOnClickListener {
                navToProfile()
            }
        }
    }

    private fun setupTab() {
        pagerAdapter = ServiceListPagerAdapter(this)
        with(binding) {
            pager.adapter = pagerAdapter
            TabLayoutMediator(tab, pager) { tabView, pos ->
                tabView.text = when(pos) {
                    0 -> resources.getString(R.string.catalog)
                    else -> resources.getString(R.string.favorite)
                }
            }.attach()
        }
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.isLogged.collect(::onLoggedChange)
            }
        }
    }

    private fun onLoggedChange(isLogged: Boolean) {
        binding.ivProfile.isVisible = isLogged
    }

    private fun navToProfile() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_serviceListFragment_to_profileFragment)
    }

    private fun showFilters() {

    }

    private fun performSearch() {

    }

    private fun hideKeyboard() {
        requireActivity().hideKeyboard(binding.searchBar)
    }
}