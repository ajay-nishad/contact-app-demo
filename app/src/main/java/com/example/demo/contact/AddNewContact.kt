package com.example.demo.contact

import android.app.Activity
import android.content.ContentProviderOperation
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import com.example.demo.R
import com.example.demo.extension.ShowToast
import kotlinx.android.synthetic.main.add_contact_activity.*


class AddNewContact : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact_activity)
        add_contact_txt.setOnClickListener {
            if (contact_name_edt.text!!.isEmpty()) {
                ShowToast("Contact name required.")
                return@setOnClickListener
            }

            if (contact_number_edt.text!!.isEmpty()) {
                ShowToast("Phone number required.")
                return@setOnClickListener
            }
            addNewContactAutomatic()
        }
    }

    private fun addNewContactAutomatic() {
        val contactNumber: String = contact_name_edt.text.toString()
        val mobileNumber: String = contact_number_edt.text.toString()
        val ops = ArrayList<ContentProviderOperation>()
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build()
        )

        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                    contactNumber
                ).build()
        )

        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                ).build()
        )

        try {
            val result  =  this.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            if (!result.isEmpty()){
                setResult(Activity.RESULT_OK)
                this.finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}