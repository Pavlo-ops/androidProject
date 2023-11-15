package com.shpp.application.level_4.presentation.fragments.viewPager_fragment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shpp.application.level_4.presentation.fragments.MyProfileFragment
import com.shpp.application.level_4.presentation.fragments.my_contacts.MyContactsFragment
import com.shpp.application.level_4.presentation.fragments.viewPager_fragment.ViewPagerFragment

class ViewPagerAdapter(mainFragment: Fragment): FragmentStateAdapter(mainFragment) {
    override fun getItemCount(): Int = ViewPagerFragment.PagerScreens.entries.size

    override fun createFragment(position: Int): Fragment {
        return when(ViewPagerFragment.PagerScreens.entries[position]) {
            ViewPagerFragment.PagerScreens.PROFILE_SCREEN -> MyProfileFragment()
            ViewPagerFragment.PagerScreens.MY_CONTACTS_SCREEN -> MyContactsFragment()
        }
    }
}