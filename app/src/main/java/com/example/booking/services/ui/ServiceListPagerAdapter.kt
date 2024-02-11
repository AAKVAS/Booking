package com.example.booking.services.ui


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Адаптер для [ViewPager2], предоставляющий списки каталога
 */
class ServiceListPagerAdapter(
    fragment: Fragment,
    private val items: List<Fragment>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
         return items[position]
    }
}