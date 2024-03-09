package com.example.booking.services.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booking.MainActivity
import com.example.booking.R
import com.example.booking.databinding.FragmentCatalogBinding
import com.example.booking.services.data.datasource.ServiceListAPI
import com.example.booking.services.data.datasource.ServiceListApiImpl
import com.example.booking.services.data.datasource.ServicePagingSource
import com.example.booking.services.data.entity.SearchParams
import com.example.booking.services.domain.model.Service
import com.example.booking.services.ui.adapters.ServiceListAdapter
import com.example.booking.services.ui.viewmodel.CatalogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Фрагмент списка услуг
 */
@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding
    private val viewModel: CatalogViewModel by viewModels()
    private val adapter: ServiceListAdapter = ServiceListAdapter(
        onItemClick = ::openServiceFragment
    )
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        search()
        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

            }
        }
    }

    private fun search(searchPattern: String = "", cityId: Long = -1) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.search(searchPattern, cityId).collect { adapter.submitData(it) }
        }
    }

    private fun bind() {
        with(binding) {
            rvServices.adapter = adapter
            rvServices.layoutManager = LinearLayoutManager(rvServices.context)
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

    private fun openServiceFragment(service: Service) {
        val action = ServiceListFragmentDirections.actionServiceListFragmentToServiceFragment(service.id)
        (requireActivity() as MainActivity).navController
            .navigate(action)
    }

    private fun showLoading() {

    }

    private fun showErrorMessage() {

    }
}