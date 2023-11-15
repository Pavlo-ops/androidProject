package com.shpp.application.level_4.presentation.fragments.my_contacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.application.R
import com.shpp.application.databinding.ItemUserBinding
import com.shpp.application.level_4.data.model.User
import com.shpp.application.level_4.presentation.callBacks.diffutil.UserDiffUtilCallBack
import com.shpp.application.level_4.presentation.multiselect.ContactItem
import com.shpp.application.level_4.presentation.interfaces.ContactSelectionListener
import com.shpp.application.level_4.presentation.interfaces.MyContactsAdapterListener
import com.shpp.application.level_4.presentation.utils.extensions.downloadAndPutPhoto
import com.shpp.application.level_4.presentation.utils.extensions.gone
import com.shpp.application.level_4.presentation.utils.extensions.visible
import com.shpp.application.level_4.utils.Constants.TRANSACTION_PHOTO

class UsersAdapter(
    val listener: MyContactsAdapterListener,
    private val contactSelectionListener: ContactSelectionListener
) : ListAdapter<User, UsersAdapter.UsersViewHolder>(UserDiffUtilCallBack()) {


    inner class UsersViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            with(binding) {
                textName.text = user.name
                textViewJob.text = user.job
                buttonBasket.tag = user // TODO: delete
                avatarUser.downloadAndPutPhoto(user.photo)

                if (contactSelectionListener.isSelectionModeEnabled()) {
                    checkboxItem.isChecked = contactSelectionListener.isCheck(user)
                    //removeSimpleListeners()
                    setMultiSelectListeners(user)
                    changeDesignForMultiSelect()
                } else {
                    setSimpleListeners(user)
                    changeDesignToSimpleState()
                }
            }
        }

        private fun removeSimpleListeners() { // TODO: remove
            with(binding) {
                if (buttonBasket.hasOnClickListeners()) {
                    buttonBasket.setOnClickListener(null)
                    root.setOnLongClickListener(null)
                }
            }
        }

        private fun setSimpleListeners(user: User) {
            with(binding) {
                root.setOnClickListener {
                    val extras = FragmentNavigatorExtras(
                        avatarUser to TRANSACTION_PHOTO
                    )
                    avatarUser.transitionName = "transaction_${user.id}"
                    listener.onClick(user, extras)
                }

                buttonBasket.setOnClickListener { listener.onDeleteClick(user) }

                root.setOnLongClickListener {
                   contactSelectionListener.enableSelectionMode()
                    contactSelectionListener.onContactSelectionActivated()
                    listener.onLongClick(ContactItem(user, true))
                    true
                }
            }
        }

        private fun setMultiSelectListeners(user: User) {
            with(binding) {
                checkboxItem.setOnClickListener {
                    contactSelectionListener.onCheckClick(user, checkboxItem.isChecked)
                    contactSelectionListener.disableSelectionMode()
                }

                root.setOnClickListener {
                    checkboxItem.isChecked = !checkboxItem.isChecked
                    contactSelectionListener.onCheckClick(user, checkboxItem.isChecked)
                    contactSelectionListener.disableSelectionMode()
                }
            }
        }

        private fun changeDesignToSimpleState() {
            with(binding) {
                buttonBasket.visible()
                checkboxItem.gone()
                val background = ContextCompat.getDrawable(binding.root.context, R.drawable.item_border)
                root.background = background
            }
        }

        private fun changeDesignForMultiSelect() {
            with(binding) {
                buttonBasket.visibility = View.GONE // TODO: use ext
                checkboxItem.visibility = View.VISIBLE
                val background = ContextCompat.getDrawable(binding.root.context, R.drawable.item_border_select)
                root.background = background
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(
            inflater,
            parent,
            false
        )
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UsersViewHolder,
        position: Int
    ) = holder.bind(currentList[position])
}
