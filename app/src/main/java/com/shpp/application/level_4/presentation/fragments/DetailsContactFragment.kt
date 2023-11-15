package com.shpp.application.level_4.presentation.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shpp.application.R
import com.shpp.application.databinding.FragmentContactDetailsBinding
import com.shpp.application.level_4.presentation.utils.extensions.downloadAndPutPhoto

class DetailsContactFragment :
    BaseFragment<FragmentContactDetailsBinding>(FragmentContactDetailsBinding::inflate) {

    private val args: DetailsContactFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSharedElementAnimation()

    }

    private fun setSharedElementAnimation() {
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(R.transition.custom_move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillUserProfile()
    }

    override fun setListeners() {
        binding.buttonBack.setOnClickListener { startViewPagerFragment() }
    }

    private fun fillUserProfile() {
        with(binding) {
            avatar.downloadAndPutPhoto(args.contact.photo)
            textViewName.text = args.contact.name
            textCareer.text = args.contact.job
            textAddress.text = args.contact.address
        }
    }

    private fun startViewPagerFragment() {
        findNavController().navigateUp()
    }
}