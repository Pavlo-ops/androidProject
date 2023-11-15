package com.shpp.application.level_4.presentation.multiselect

import com.shpp.application.level_4.data.model.User
import java.util.UUID

data class ContactItem (
    val originUser: User,
    var isChecked: Boolean
) {
    val id: UUID get() = originUser.id
    val name: String get() = originUser.name

    // TODO: remove not used variables
    val job: String get() = originUser.job
    val address: String get() = originUser.address
    val email: String get() = originUser.email
    val birth: String get() = originUser.birth
    val phone: String get() = originUser.phone
    val photo: String? get() = originUser.photo
}