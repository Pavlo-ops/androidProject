package com.shpp.application.level_4.presentation.fragments.my_contacts

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shpp.application.R
import com.shpp.application.databinding.FragmentMyContactsBinding
import com.shpp.application.level_4.data.model.User
import com.shpp.application.level_4.presentation.callBacks.SwipeToDeleteCallback
import com.shpp.application.level_4.presentation.fragments.BaseFragment
import com.shpp.application.level_4.presentation.fragments.viewPager_fragment.ViewPagerFragment
import com.shpp.application.level_4.presentation.fragments.my_contacts.adapter.UsersAdapter
import com.shpp.application.level_4.presentation.fragments.my_contacts.add_contact.ContactDialog
import com.shpp.application.level_4.presentation.multiselect.ContactItem
import com.shpp.application.level_4.presentation.fragments.viewPager_fragment.ViewPagerFragmentDirections
import com.shpp.application.level_4.presentation.interfaces.ContactSelectionListener
import com.shpp.application.level_4.presentation.interfaces.MyContactsAdapterListener
import com.shpp.application.level_4.presentation.utils.extensions.invisible
import com.shpp.application.level_4.presentation.utils.extensions.visibleIf
import com.shpp.application.level_4.utils.Constants
import com.shpp.application.level_4.utils.Constants.ADD_USER_TAG

class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {

    private val viewModel: MyContactsViewModel by viewModels()

    private val adapter: UsersAdapter by lazy {
        UsersAdapter(
            listener = object : MyContactsAdapterListener {
                override fun onClick(contact: User, extras: FragmentNavigator.Extras) {
                    startDetailFragment(contact, extras)
                }

                override fun onDeleteClick(contact: User) {
                    viewModel.deleteUser(contact)
                    showSnackBar(
                        getString(R.string.snackbar_removed),
                        R.string.snackbar_undo
                    ) { viewModel.restoreLastDeletedUser() }
                }

                override fun onLongClick(contactItem: ContactItem) {
                    if (!viewModel.isSelectionModeEnabled()) {
                        viewModel.enableSelectionMode()
                    }
                    viewModel.toggle(contactItem)
                }
            },

            contactSelectionListener = object : ContactSelectionListener {
                override fun onContactSelectionActivated() {
                    binding.buttonMultiDelete.visibleIf(viewModel.isSelectionModeEnabled())
                }

                override fun isCheck(user: User): Boolean {
                    return viewModel.isCheck(user)
                }

                override fun onCheckClick(contact: User, isChecked: Boolean) {
                    viewModel.toggle(ContactItem(contact, isChecked))
                }

                override fun disableSelectionMode() {
                    doDisableSelectionMode()
                }

                override fun enableSelectionMode() {
                    viewModel.enableSelectionMode()
                }

                override fun isSelectionModeEnabled(): Boolean {
                    return viewModel.isSelectionModeEnabled()
                }
            }
        )
    }

    private fun doDisableSelectionMode() {
        viewModel.disableSelectionMode()
        if (!viewModel.isSelectionModeEnabled()) {
            binding.buttonMultiDelete.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        restoreButtonMultiDeleteState()
    }

    private fun restoreButtonMultiDeleteState() {
        if (viewModel.isSelectionModeEnabled()) {
            binding.buttonMultiDelete.visibility = View.VISIBLE
        }
    }

    override fun setListeners() {
        addVisibleButtonScrollListener()
        addMultiDeleteListener()
        addScrollClickedListener()
        addListenerAddContact()
        addListenerBackToProfile()
    }

    private fun addMultiDeleteListener() {
        binding.buttonMultiDelete.setOnClickListener {
            viewModel.deleteSelectedContacts()
            doDisableSelectionMode()
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.users.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            viewModel.updateContactsSelectionMode()
        }
        selectModeObserve()
    }

    private fun selectModeObserve() {
        var recyclerState: Parcelable?

        // Reload RecyclerView and save the scroll state.
        viewModel.getSelectionModeLiveData().observe(viewLifecycleOwner) { selectionMode ->
            recyclerState = binding.recyclerUsers.layoutManager?.onSaveInstanceState()

            binding.recyclerUsers.adapter = adapter

            if (!selectionMode) {
                binding.recyclerUsers.layoutManager?.onRestoreInstanceState(recyclerState)
            }

            // changeEnablingTouchHelperTo(selectionMode)
        }
    }

//    private fun changeEnablingTouchHelperTo(enabling: Boolean) {
//        if (enabling) {
//            itemTouchHelper?.attachToRecyclerView(null)
//        } else {
//            itemTouchHelper?.attachToRecyclerView(binding.recyclerUsers)
//        }
//    }

    private fun addListenerBackToProfile() {
        binding.buttonBack.setOnClickListener {
            (parentFragment as ViewPagerFragment).switchToPage(ViewPagerFragment.PagerScreens.PROFILE_SCREEN)
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerUsers.adapter = adapter
            setSwipeToDelete()
        }
    }

    private fun setSwipeToDelete() {

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(
            onSwiped = { position ->
                viewModel.deleteUserByPosition(position)
                showSnackBar(
                    "Remove!", R.string.snackbar_undo // TODO: res
                ) { viewModel.restoreLastDeletedUser() }
            },
            isEnabled = { !viewModel.isSelectionModeEnabled() }
        ))
        itemTouchHelper.attachToRecyclerView(binding.recyclerUsers)
    }


    private fun addListenerAddContact() {
        binding.buttonAddContacts.setOnClickListener {
            val dialogAddUser = ContactDialog()
            dialogAddUser.show(parentFragmentManager, ADD_USER_TAG)
        }
    }

    private fun addScrollClickedListener() {
        binding.buttonScroll.setOnClickListener {
            binding.recyclerUsers.smoothScrollToPosition(Constants.ZERO_POSITION)
        }
    }

    private fun addVisibleButtonScrollListener() {
        with(binding) {
            recyclerUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // if the recycler view is scrolled above hide the button
                    if (dy > 10 && buttonScroll.visibility == View.VISIBLE) {
                        buttonScroll.invisible()
                    }

                    // if the recycler view is scrolled above show the button
                    if (dy < -10 && buttonScroll.visibility == View.INVISIBLE) {
                        buttonScroll.visibility = View.VISIBLE
                    }

                    // if the recycler view is at the first item always show the button
                    if (!recyclerView.canScrollVertically(-1)) {
                        buttonScroll.visibility = View.INVISIBLE
                    }
                }
            })
        }
    }

    private fun startDetailFragment(contact: User, extras: FragmentNavigator.Extras) {
        val direction =
            ViewPagerFragmentDirections.actionViewPagerFragmentToDetailsContactFragment( // TODO: send model
                contact
            )
        findNavController().navigate(direction, extras)
    }
}