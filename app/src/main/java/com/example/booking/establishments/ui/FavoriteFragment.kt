package com.example.booking.establishments.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.booking.MainActivity
import com.example.booking.databinding.FragmentFavoriteEstablishmentBinding
import com.example.booking.establishments.domain.model.Establishment
import com.example.booking.establishments.ui.adapters.EstablishmentListAdapter
import com.example.booking.establishments.ui.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * Фрагмент избранных услуг
 */
@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteEstablishmentBinding
    private val viewModel: FavoriteViewModel by viewModels()
    private val adapter: EstablishmentListAdapter = EstablishmentListAdapter(
        onItemClick = ::openFavoriteFragment
    )
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteEstablishmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        subscribeSearch()
    }

    private fun subscribeSearch() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                val cityIdStateFlow = (parentFragment as CatalogFragment).filterCityId
                val searchPatternStateFlow = (parentFragment as CatalogFragment).searchPattern
                combine(cityIdStateFlow, searchPatternStateFlow) { cityId, searchPattern ->
                    search(cityId, searchPattern)
                }.collect()
            }
        }
    }

    private fun search(cityId: Long = -1, searchPattern: String = "") {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.search(searchPattern, cityId).collect { adapter.submitData(it) }
        }
    }

    private fun bind() {
        with(binding) {
            recyclerViewEstablishment.adapter = adapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    when(loadStates.refresh) {
                        is LoadState.Loading -> showLoading()
                        is LoadState.Error -> showErrorMessage()
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showLoading() {

    }

    private fun showErrorMessage() {

    }

    private fun openFavoriteFragment(establishment: Establishment) {
        val action = CatalogFragmentDirections.actionCatalogFragmentToEstablishmentFragment(establishment.id)
        (requireActivity() as MainActivity).navController
            .navigate(action)
    }
}