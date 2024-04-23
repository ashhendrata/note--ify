package com.example.notify


import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.LinearLayout



class MyCircle : AppCompatActivity() {
    private lateinit var adapter: ContactsAdapter  // Declare the adapter at the class level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contacts)

        val recyclerView = findViewById<RecyclerView>(R.id.contactsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val contacts = mutableListOf("Alice", "Bob", "Charlie", "David")
        adapter = ContactsAdapter(contacts) { position ->
            Toast.makeText(this, "Selected: ${contacts[position]}", Toast.LENGTH_SHORT).show()
            // Create an intent to start NextActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val fab: FloatingActionButton = findViewById(R.id.fab_add_contact)
        fab.setOnClickListener {
            showAddContactDialog()
        }
    }

    private fun showAddContactDialog() {
        val context = this  // For clarity in dialog construction

        // Creating EditText for name and phone number
        val nameInput = EditText(context).apply {
            hint = "Name"
        }
        val phoneInput = EditText(context).apply {
            hint = "Phone Number"
            inputType = android.text.InputType.TYPE_CLASS_PHONE
        }

        // Creating a LinearLayout to hold EditTexts
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)  // Adjust padding as needed
            addView(nameInput)
            addView(phoneInput)
        }

        // Build and show the AlertDialog
        AlertDialog.Builder(context)
            .setTitle("Add New Contact")
            .setMessage("Enter the details of the new contact:")
            .setView(layout)
            .setPositiveButton("Add") { dialog, which ->
                val name = nameInput.text.toString()
                val phone = phoneInput.text.toString()
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    adapter.addContact("$name - $phone")  // Example format
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}
class ContactsAdapter(
    private val contactsList: MutableList<String>,
    private val onClick: (Int) -> Unit) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName: TextView = view.findViewById(R.id.contact_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactsList[position]
        holder.contactName.text = contact
        holder.itemView.setOnClickListener {
            onClick(position)  // Pass position to the lambda
        }
    }

    override fun getItemCount() = contactsList.size

    fun addContact(contact: String) {
        contactsList.add(contact)
        notifyItemInserted(contactsList.size - 1)
    }
}
