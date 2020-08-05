package com.example.everartstudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cancelled__order.*
import kotlinx.android.synthetic.main.activity_conform__orders.*

class Cancelled_Order : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancelled__order)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Myorder")
        var arlst:ArrayList<MyOrder> = arrayListOf()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                arlst.clear()
                for (v1 in dataSnapshot.children){
                    for (v2 in v1.children){


                        if (v2.child("status").value.toString() == "cancelled"){

                            val value =v2.getValue(MyOrder::class.java)!!
                            if(value != null){
                                arlst.add(value)
                            }
                        }

                    }
                }
                var adapter = ConformOrderMainClass(this@Cancelled_Order,arlst)
                rcv_cancelled_order.adapter = adapter
                rcv_cancelled_order.layoutManager = LinearLayoutManager(this@Cancelled_Order,
                    LinearLayoutManager.VERTICAL,false)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })

    }
}