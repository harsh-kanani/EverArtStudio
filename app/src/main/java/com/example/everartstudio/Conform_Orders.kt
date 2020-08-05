package com.example.everartstudio

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_conform__orders.*


class Conform_Orders : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conform__orders)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Myorder")
        var arlst:ArrayList<MyOrder> = arrayListOf()

        // Read from the database

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                arlst.clear()
                for (v1 in dataSnapshot.children){
                    for (v2 in v1.children){


                        if (v2.child("status").value.toString() == "conform"){

                            val value =v2.getValue(MyOrder::class.java)!!
                            if(value != null){
                                arlst.add(value)
                            }
                        }

                    }
                }
                var adapter = ConformOrderMainClass(this@Conform_Orders,arlst)
                rcv_conform_orders.adapter = adapter
                rcv_conform_orders.layoutManager = LinearLayoutManager(this@Conform_Orders,LinearLayoutManager.VERTICAL,false)
                
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })

    }
}