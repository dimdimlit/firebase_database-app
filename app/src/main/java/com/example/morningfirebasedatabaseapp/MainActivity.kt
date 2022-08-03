package com.example.morningfirebasedatabaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var editTextName: EditText? = null
    var editTextEmail: EditText? = null
    var editTextIdNumber: EditText? = null
    var buttonSave: Button? = null
    var buttonView: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Finding the views by id
        editTextName = findViewById(R.id.mEdtName)
        editTextEmail = findViewById(R.id.mEdtEmail)
        editTextIdNumber = findViewById(R.id.mEdtIdNumber)
        buttonSave = findViewById(R.id.mBtnSave)
        buttonView = findViewById(R.id.mBtnView)

        //Setting the listeners
        buttonSave!!.setOnClickListener {
            var userName = editTextName!!.text.toString().trim()
            var userEmail = editTextEmail!!.text.toString().trim()
            var userIdNumber = editTextIdNumber!!.text.toString().trim()
            var id = System.currentTimeMillis().toString()

                        //Checking if a user has submitted an empty field
            if (userName.isEmpty()) {
                editTextName!!.setError("PLease fill this field!!")
                editTextName!!.requestFocus()
            } else if (userEmail.isEmpty()) {
                editTextEmail!!.setError("Please fill this field!!")
                editTextEmail!!.requestFocus()
            } else if (userIdNumber.isEmpty()) {
                editTextIdNumber!!.setError("Please fill this field!!")
                editTextIdNumber!!.requestFocus()
            } else {
                        //Proceed to save data
                            //Start by creating the user object
                val userData = User(userName, userEmail, userIdNumber, id)
                                //Creating a reference to the database to store the data
                            //Does not change in any circumstance
                val reference = FirebaseDatabase.getInstance().getReference().child("Users/$id")
                                //Start saving user data
                reference.setValue(userData).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext, "Data saved successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext, "Data saving Failed!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        }


        //---------------\\
        buttonView!!.setOnClickListener {
            val goToUsers = Intent(applicationContext, UsersActivity::class.java)
            startActivity(goToUsers)
        }
        //---------------\\


    }
}