package com.example.demo.contact

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.R
import com.example.demo.adapter.ContactAdapter
import com.example.demo.extension.ShowToast
import com.example.demo.model.Contact
import kotlinx.android.synthetic.main.activity_contact.*


class ContactActivity : AppCompatActivity() {
    private val RE: Int = 101
    var mContact: MutableList<Contact> = mutableListOf()

    private var PERMISSIONS = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        progressBar.visibility = View.VISIBLE
        initView()

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    getUserContact()
                }
            }

        fab_add_contact.setOnClickListener {
            val intent = Intent(this@ContactActivity, AddNewContact::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun initView() {
        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 101)
        } else {
            getUserContact()
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun getUserContact() {
        val userList: MutableList<Contact> = mutableListOf()
        val cursor: Cursor? =
            contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" ASC"
            )


        Thread(Runnable {
            while (cursor!!.moveToNext()) {
                val name: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                userList.add(Contact(name, phoneNumber))
                mContact = userList.distinctBy { it.name } as MutableList<Contact>
            }

            cursor.close()

            runOnUiThread {
                progressBar.visibility = View.GONE
                val layoutManager = contactRecyclerView.layoutManager as LinearLayoutManager
                val divider = DividerItemDecoration(
                    contactRecyclerView.getContext(),
                    layoutManager.orientation
                )
                contactRecyclerView.addItemDecoration(divider)
                contactRecyclerView.adapter = ContactAdapter(mContact)
            }
        }).start()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    getUserContact()
                } else {
                    ShowToast("Please allow permission")
                }
            }
        }
    }
}