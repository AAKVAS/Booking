package com.example.booking.services.ui


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ServiceListPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val items: List<Fragment> = listOf(CatalogFragment(), FavoriteFragment())

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
         return items[position]
    }
}