package com.example.morningfirebasedatabaseapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersActivity : AppCompatActivity() {
    var listUsers:ListView ?= null
    var adapter:CustomAdapter ?= null
    var users:ArrayList<User> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

       //Find view by id
       listUsers = findViewById(R.id.mListUsers)
       users = ArrayList()
       adapter = CustomAdapter(this,users!!)

        //Connect to the users table/child to fetch data
        val reference = FirebaseDatabase.getInstance().
                                            getReference().child("Users")

        //Start fetching the data
        reference.addValueEventListener(object  : ValueEventListener{

            //Override the on data changed
            override fun onDataChange(snapshot: DataSnapshot) {
                users!!.clear()

                //Use the for loop to add the users on the arraylist
                for (snap in snapshot.children){
                    var user = snap.getValue(User::class.java)
                    users!!.add(user!!)
                }
                adapter!!.notifyDataSetChanged()
            }

            //Override the on Cancelled method
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"Please contact the admin",
                            Toast.LENGTH_LONG).show()
            }
        })

        //Display the users
        listUsers!!.adapter = adapter

        //Set an on item click listener to the listview
        listUsers!!.setOnItemClickListener { adapterView, view, i, l ->
            val userId = users!!.get(i).id
            val deletetionReference = FirebaseDatabase.getInstance().
                                        getReference().child("Users/$userId")

            //Set an alert when one clicks on an item
            val alertDialog =  AlertDialog.Builder(this)
            alertDialog.setTitle("ALERT!!!")
            alertDialog.setMessage("Select an option you want to perform ")
            alertDialog.setNegativeButton("Update",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        //Dismiss the alert
                    })
            alertDialog.setPositiveButton("Delete",DialogInterface.OnClickListener { dialogInterface, i ->
                deletetionReference.removeValue()
                Toast.makeText(applicationContext,"Deleted successfully",
                                                    Toast.LENGTH_LONG).show()
            })
            alertDialog.create().show()
        }
    }
}