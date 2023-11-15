package com.shpp.application.level_4.presentation.fragments.my_contacts.add_contact

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.shpp.application.databinding.AddUserDialogBinding
import com.shpp.application.level_4.data.model.User
import com.shpp.application.level_4.presentation.utils.extensions.downloadAndPutPhoto

/**
 * ContactDialog.kt
 * @author Pavlo Kokhanevych
 */
class ContactDialog : DialogFragment() {

    private val addContactViewModel: AddContactViewModel by viewModels()
    private val bindingAdd: AddUserDialogBinding by lazy {
        AddUserDialogBinding.inflate(layoutInflater)
    }

    private var urlAvatar: String = ""

    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultForActivity ->
            downloadImage(resultForActivity)
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(bindingAdd.root)
        //initializeResultLauncher()
        setListeners()
        return builder.create()
    }

    private fun setListeners() {
        with(bindingAdd) {
            buttonAddPhoto.setOnClickListener { openPhotoList() }
            buttonSave.setOnClickListener { addNewUser() }
        }
        addBaselineListener()
    }

    private fun openPhotoList() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }


    private fun addBaselineListener() {
        bindingAdd.buttonBack.setOnClickListener {
            dismiss()
        }
    }

    private fun addNewUser() {
        with(bindingAdd) {
            val user = User(
                name = editTextUsername.text.toString(),
                job = editTextCareer.text.toString(),
                address = editTextAddress.text.toString(),
                email = editTextEmail.text.toString(),
                birth = editTextBirth.text.toString(),
                phone = editTextPhone.text.toString(),
                photo = urlAvatar
            )
            Log.d("myLog", user.toString())
            addContactViewModel.addUser(user)
            dismiss()
        }
    }

    private fun initializeResultLauncher() {
        // resultLauncher =

    }

    private fun downloadImage(result: androidx.activity.result.ActivityResult) {

        // Checks ending operation choosing photo.
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val selectedImageUri = data.data

                // Loads photo and saves URL.
                if (selectedImageUri != null) {
                    urlAvatar = selectedImageUri.toString()
                    bindingAdd.avatar.downloadAndPutPhoto(urlAvatar)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        }
    }
}
