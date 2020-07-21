package com.example.everartstudio

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_view__product.*
import java.util.*
import kotlin.collections.ArrayList


class View_Product : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view__product)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Product")
        var arlst:ArrayList<AddProductDataClass> = arrayListOf<AddProductDataClass>()
        var diaplay:ArrayList<AddProductDataClass> = arrayListOf()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                arlst.clear()
                for(p in dataSnapshot.children){
                    var value =p.getValue(AddProductDataClass::class.java)
                    if(value != null){
                        arlst.add(value)

                    }
                    diaplay.addAll(arlst)
                    var ad=ViewProductMainClass(this@View_Product,diaplay)
                    rcv_view_product.adapter=ad
                    rcv_view_product.layoutManager =LinearLayoutManager(this@View_Product,LinearLayoutManager.VERTICAL,false)
                    search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (newText !!.isNotEmpty()){
                                diaplay.clear()
                                var sr = newText.toLowerCase(Locale.getDefault())
                                arlst.forEach{
                                    if(it.product.toLowerCase(Locale.getDefault()).contains(sr)){
                                        diaplay.add(it)
                                    }
                                }
                                rcv_view_product.adapter!!.notifyDataSetChanged()
                            }
                            else{
                                diaplay.clear()
                                diaplay.addAll(arlst)
                                rcv_view_product.adapter!!.notifyDataSetChanged()
                            }
                            return true
                        }

                    })

                }
                //Log.d(FragmentActivity.TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }
}