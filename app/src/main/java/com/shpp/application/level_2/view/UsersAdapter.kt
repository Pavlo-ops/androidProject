package com.shpp.application.level_2.view

import android.content.Context
import android.os.CountDownTimer
import android.provider.Settings.Secure.getString
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.shpp.application.R
import com.shpp.application.databinding.ItemUserBinding
import com.shpp.application.databinding.MyContactsActivityBinding
import com.shpp.application.level_2.App
import com.shpp.application.level_2.model.User
import com.shpp.application.level_2.viewModel.MyContactsViewModel
import com.squareup.picasso.Picasso


class UserDiffCallBack(
    private val oldList: List<User>,
    private val newList: List<User>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}

class UsersAdapter(
    val myContactsViewModel: MyContactsViewModel,
    private val contactsBinding: MyContactsActivityBinding,
    private val massageSnackBar: String
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {


    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var users: List<User> = emptyList()
        set(value) {
            val diffUtil = UserDiffCallBack(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            field = value;
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding) {
            name.text = user.name
            job.text = user.job
            basket.tag = user
            basket.setOnClickListener(this@UsersAdapter)
            avatarUser.downloadAndPutPhotoGlide(avatarUser, R.drawable.ic_user_avatar, user.photo)
        }
    }

    private fun ImageView.downloadAndPutPhotoGlide(
        imageView: ImageView,
        defauldPhotoId: Int,
        link: String
    ) {
        if (link.isNotBlank()) {
            Glide.with(imageView.context)
                .load(link)
                .circleCrop()
                .placeholder(defauldPhotoId)
                .error(defauldPhotoId)
                .into(imageView)
        } else {
            imageView.setImageResource(defauldPhotoId)
        }
    }

    private fun ImageView.downloadAndPutPhotoPicasso(
        imageView: ImageView,
        defauldPhotoId: Int,
        link: String
    ) {
        if (link.isNotBlank()) {
            Picasso.with(imageView.context)
                .load(link)
                .placeholder(defauldPhotoId)
                .error(defauldPhotoId)
                .into(this)
        } else {
            imageView.setImageResource(defauldPhotoId)
        }
    }

    override fun getItemCount(): Int = users.size

    override fun onClick(v: View) {
        val user = v.tag as User
        when (v.id) {
            R.id.basket -> {
                val index: Int = myContactsViewModel.users.value?.indexOf(user) ?: -1;
                myContactsViewModel.deleteUser(user)
                undoDelete(user, index)
            }
        }
    }

     fun undoDelete(user: User, index: Int) {
        val snackBar = Snackbar.make(contactsBinding.myContactsView, R.string.snackbar_removed, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(R.string.snackbar_undo) {
            myContactsViewModel.addUser(user, index)
        }

        val timer = object : CountDownTimer(5000, 1000) { // 5000 мс = 5 секунд
            override fun onTick(millisUntilFinished: Long) {
                // Вивід таймеру
                snackBar.setText(massageSnackBar + " ${millisUntilFinished / 1000}")
            }

            override fun onFinish() {
                snackBar.dismiss() // Закриття SnackBar після завершення таймера
            }
        }
        timer.start()
        snackBar.show()
    }
}