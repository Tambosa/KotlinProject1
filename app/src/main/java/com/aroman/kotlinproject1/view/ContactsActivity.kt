package com.aroman.kotlinproject1.view

import android.Manifest
import android.content.ContentResolver
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.aroman.kotlinproject1.R

class ContactsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        checkPermission()
    }

    private fun checkPermission() {
        permissionResult.launch(Manifest.permission.READ_CONTACTS)
    }

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> getContacts()
                !shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> AlertDialog.Builder(
                    this
                ).setTitle("Permission")
                    .setMessage("Read Contacts")
                    .setPositiveButton("Yes") { _, _ ->
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            42
                        )
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
                else -> {
                    Toast.makeText(this, "T_T", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun getContacts() {
        val contentResolver: ContentResolver = contentResolver
        val contactsTextView = findViewById<TextView>(R.id.contacts_text_view).apply {
            text = ""
        }

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Data.DISPLAY_NAME + " ASC",
        )

        cursor?.let {
            val columnIndex = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)
            if (columnIndex >= 0) {
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val name = cursor.getString(columnIndex)
                        contactsTextView.text = "${contactsTextView.text}${name}\n"
                    }
                }
            }
            cursor.close()
        }
    }
}