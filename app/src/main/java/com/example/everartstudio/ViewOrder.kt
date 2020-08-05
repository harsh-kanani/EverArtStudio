package com.example.everartstudio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.view_order.*


class ViewOrder : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_order)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Myorder")
        var arlst:ArrayList<MyOrder> = arrayListOf()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var t:Int =0
                var p = 0
                var c= 0
                arlst.clear()
                for (v1 in dataSnapshot.children){
                    for (v2 in v1.children){
                        t+=1
                        if (v2.child("status").value.toString() == "Cancelled"){
                            c+=1
                        }
                        if (v2.child("status").value.toString() == "pending"){
                            p+=1
                            val value =v2.getValue(MyOrder::class.java)!!
                            if(value != null){
                                arlst.add(value)
                            }
                        }

                    }
                }
                var adapter = PendingOrderMainClass(this@ViewOrder,arlst)
                rcv_Pending_Order.adapter =adapter
                rcv_Pending_Order.layoutManager = LinearLayoutManager(this@ViewOrder,LinearLayoutManager.VERTICAL,false)
                lblTotalOrder.text = "Total Orders : "+t.toString()
                lblCancelledOrder.text ="Cancelled Orders : "+ c.toString()
                lblPendingOrder.text = "Pending Orders : "+p.toString()

                //Log.d(FragmentActivity.TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menuAddProduct->{
                startActivity(Intent(this@ViewOrder,AddProduct::class.java))
                return true
            }
            R.id.menuViewProduct->{
                startActivity(Intent(this@ViewOrder,View_Product::class.java))
                return true
            }
            R.id.menuQuery->{
                return true
            }
            R.id.menuLogout->{
                startActivity(Intent(this@ViewOrder,Login::class.java))
                return true
            }
            else->super.onOptionsItemSelected(item)
        }

    }
}