package com.shpp.application.level_4.presentation.fragments.viewPager_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.shpp.application.R
import com.shpp.application.databinding.FragmentViewpagerBinding
import com.shpp.application.level_4.presentation.fragments.viewPager_fragment.adapter.ViewPagerAdapter

class ViewPagerFragment : Fragment() {

    private val binding: FragmentViewpagerBinding by lazy {
        FragmentViewpagerBinding.inflate(layoutInflater)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewPager.adapter = ViewPagerAdapter(this)
        attachTabLayout()
        return binding.root
    }

    private fun attachTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (PagerScreens.entries[position]) {
                PagerScreens.PROFILE_SCREEN -> getString(R.string.profile_screen)
                PagerScreens.MY_CONTACTS_SCREEN -> getString(R.string.my_contacts_screen)
            }
        }.attach()
    }

    fun switchToPage(screen: PagerScreens) {
        binding.viewPager.currentItem = screen.ordinal
    }

    enum class PagerScreens {
        PROFILE_SCREEN,
        MY_CONTACTS_SCREEN
    }
}