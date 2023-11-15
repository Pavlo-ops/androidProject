package com.shpp.application.level_4.presentation.multiselect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shpp.application.level_4.data.model.User

class MultiSelectManager(
    users: List<User>?
) {
    private val _contactsLiveData = MutableLiveData<List<ContactItem>>()
    val contactsLiveData: LiveData<List<ContactItem>> get() = _contactsLiveData

    val selectionModeLiveData: LiveData<Boolean> get() = _selectionModeLiveData
    private val _selectionModeLiveData = MutableLiveData<Boolean>()

    init {
        updateContactsSelectionMode(users)
        _selectionModeLiveData.value = false
    }

    fun enableSelectionMode() {
        _selectionModeLiveData.value = true
    }

    fun disableSelectionMode() {
        val hasLeastOneCheckContact = _contactsLiveData.value?.find { it.isChecked }
        if (hasLeastOneCheckContact == null) {
            _selectionModeLiveData.value = false
        }
    }

    fun toggle(contactItem: ContactItem) {
        val updatedContacts = _contactsLiveData.value?.toMutableList() ?: mutableListOf()
        val contactIndex = updatedContacts.indexOf(updatedContacts.find { contactItem.id == it.id })

        if (contactIndex != -1) {
            updatedContacts[contactIndex] = contactItem
            _contactsLiveData.value = updatedContacts
        }
    }

    fun isCheck(user: User): Boolean {
        val contactItem = _contactsLiveData.value?.find { user.id == it.id }
        if (contactItem != null) {
            return contactItem.isChecked
        }
        return false
    }

    /** Compare fake list contacts with users list from DATA **/
    fun updateContactsSelectionMode(users: List<User>?) {
        if (_contactsLiveData.isInitialized) {
            _contactsLiveData.value = users?.map { user ->
                ContactItem(user, _contactsLiveData.value?.find {  it.id == user.id}?.isChecked ?: false)
            } ?: emptyList()
        } else {
            _contactsLiveData.value = users?.map { ContactItem(it, false) } ?: emptyList()
        }
    }
}