package com.example.booking.establishments.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.booking.MainActivity
import com.example.booking.R
import com.example.booking.common.utils.hideKeyboard
import com.example.booking.common.utils.isNetworkAvailable
import com.example.booking.common.utils.showNetworkNotAvailableMessage
import com.example.booking.databinding.FragmentCatalogBinding
import com.example.booking.establishments.ui.viewmodel.CatalogViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Фрагмент списка всех и избранных заведений
 */
@AndroidEntryPoint
class CatalogFragment : Fragment() {
    private lateinit var binding: FragmentCatalogBinding

    private lateinit var pagerAdapter: CatalogPagerAdapter

    private val viewModel: CatalogViewModel by viewModels()

    /**
     * Id города, по которому необходимо искать филиалы заведений
     */
    val filterCityId: StateFlow<Long>
        get() = viewModel.cityId

    /**
     * Строка для поиска филиалов заведений
     */
    val searchPattern: StateFlow<String>
        get() = viewModel.searchPattern

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopBar()
        subscribeToViewModel()
    }

    override fun onResume() {
        super.onResume()
        val context = requireContext()
        if (!context.isNetworkAvailable()) {
            context.showNetworkNotAvailableMessage()
        } else {
            viewModel.checkServiceAvailable()
        }
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

    private fun getPagerItems(): List<Fragment> {
        return if (viewModel.isLogged.value) {
            listOf(EstablishmentListFragment(), FavoriteFragment())
        } else {
            listOf(EstablishmentListFragment())
        }
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.isLogged.collect(::onLoggedChange) }
                launch { viewModel.isServiceAvailable.collect(::serviceAvailabilityChanged) }
            }
        }
    }

    private fun onLoggedChange(isLogged: Boolean) {
        binding.ivProfile.isVisible = isLogged
        setupTab()
    }

    private fun serviceAvailabilityChanged(available: Boolean) {
        with(binding) {
            includeServiceUnavailable.serviceUnavailableLayout.isVisible = !available
            layoutContainer.isVisible = available
        }
    }

    private fun setupTab() {
        pagerAdapter = CatalogPagerAdapter(this, getPagerItems())
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

    private fun navToProfile() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_catalogFragment_to_profileFragment)
    }

    private fun showFilters() {
        val cities = viewModel.cities.value
        var selectedIndex = cities.indexOfFirst { it.id == viewModel.cityId.value}
        val items: Array<CharSequence> = cities.map { it.name }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.city)
        builder.setSingleChoiceItems(items, selectedIndex) { _, which ->
            selectedIndex = which
        }.setPositiveButton(R.string.ok) { _, _ ->
            viewModel.updateCityId(cities[selectedIndex].id)
        }.setNegativeButton(R.string.cancel) { _, _ -> }

        val dialog = builder.create()
        dialog.show()
    }

    private fun performSearch() {
        viewModel.updateSearchPattern(binding.searchBar.text.toString())
    }

    private fun hideKeyboard() {
        requireActivity().hideKeyboard(binding.searchBar)
    }
}