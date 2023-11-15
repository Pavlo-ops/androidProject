package com.shpp.application.level_4.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.shpp.application.databinding.FragmentMyProfileBinding
import com.shpp.application.level_4.App
import com.shpp.application.level_4.presentation.activities.AuthActivity
import com.shpp.application.level_4.presentation.fragments.viewPager_fragment.ViewPagerFragment

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {

    private val viewModel: MyProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeTextNameView()
    }

    private fun changeTextNameView() {
        val email = App.email
        if (email != null) {
            binding.textName.text = viewModel.getNameByEmail(email)
        }
    }


    override fun setListeners() {
        binding.buttonLogOut.setOnClickListener { startAuthActivity() }
        binding.buttonViewContact.setOnClickListener {
            (parentFragment as ViewPagerFragment).switchToPage(ViewPagerFragment.PagerScreens.MY_CONTACTS_SCREEN)
        }
    }

    private fun startAuthActivity() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
    }
}
