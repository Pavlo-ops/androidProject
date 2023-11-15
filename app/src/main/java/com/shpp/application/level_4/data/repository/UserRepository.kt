package com.shpp.application.level_4.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.javafaker.Faker
import com.shpp.application.level_4.data.model.User
import com.shpp.application.level_4.presentation.multiselect.ContactItem
import com.shpp.application.level_4.utils.Constants

/**
 * UserService.kt
 * @author Pavlo Kokhanevych
 */
class UserRepository {

    private var _users: MutableLiveData<List<User>> = MutableLiveData()
    var users: LiveData<List<User>> = _users


    private var lastDeletedUser: Pair<User, Int>? = null

    init {
        _users.postValue(generateFakeContactsList())
    }

    private fun generateFakeContactsList(): List<User> {
        val faker: Faker = Faker.instance()
        return (0..Constants.NUMBER_USERS).map {
            User(
                name = faker
                    .name()
                    .name(),
                job = faker
                    .job()
                    .position(),
                address = faker
                    .address()
                    .fullAddress(),
                email = faker
                    .random()
                    .toString(),
                birth = faker
                    .date()
                    .birthday()
                    .toString(),
                photo = IMAGES.random(),
                phone = faker
                    .phoneNumber()
                    .phoneNumber()
            )
        }
    }

    fun restoreLastDeletedUser() {
        lastDeletedUser?.let { user ->
            addUser(user.first, user.second)
            lastDeletedUser = null
        }
    }

    fun deleteUser(user: User) {
        if (user == lastDeletedUser?.first) return
        _users.value?.let { currentList ->
            lastDeletedUser = user to currentList.indexOf(user)
            _users.value =
                currentList.toMutableList().apply {
                    remove(user)
                }
        }
    }

    fun addUser(
        user: User,
        index: Int? = null
    ) {
        _users.value?.let { currentList ->
            _users.value =
                currentList.toMutableList().apply {
                    index?.let { nonNullIndex ->
                        add(nonNullIndex, user)
                    } ?: add(user)
                }
        }
    }

    fun deleteSelectedItems(contactItems: List<ContactItem>?) {
        if (contactItems == null) return
        _users.value = _users.value?.filter { user ->
            user.id == (contactItems.find { it.id == user.id && !it.isChecked }?.id ?: -1)
        }
    }

    companion object {
        private val IMAGES = listOf(
            "https://gravatar.com/avatar/ca09089ce4e4f4a5c1c408ab22d22a91?s=400&d=robohash&r=x",
            "https://gravatar.com/avatar/efd6858a5f746140ea07cec5733c7c74?s=400&d=robohash&r=x",
            "https://gravatar.com/avatar/8d615a2a1b27c4c3297fa5124305cbfc?s=400&d=robohash&r=x",
            "https://gravatar.com/avatar/a7bb3266897ad708becc0a5eaff0b557?s=400&d=robohash&r=x",
            "https://gravatar.com/avatar/e8f8c55bac6dc304540845c281f3783b?s=400&d=robohash&r=x",
            "https://gravatar.com/avatar/3a6bdb5d04f18e2fa215b69cc43a50c4?s=400&d=robohash&r=x",
            "https://gravatar.com/avatar/ba79001e355f4092cd4b47df9d1070a2?s=400&d=robohash&r=x"
        )
    }
}