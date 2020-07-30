package com.example.everartstudio

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment__cart.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Cart.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Cart : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__cart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        var sp = context!!.getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var user = sp.getString("uid","null")
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Cart").child(user.toString())
        var arlst:ArrayList<CartDataClass> = arrayListOf()
        var t = 0
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arlst.clear()
                for(i in dataSnapshot.children){
                    var value =i.getValue(CartDataClass::class.java)!!
                    if (value != null){
                        arlst.add(value)

                    }
                    t+=i.child("price").value.toString().toInt()
                }

                    lblTotal.text = t.toString()
                    lblGtotal.text= t.toString()


                var adapter = CartMainClass(context!!,arlst,user.toString())
                rcvCart.adapter = adapter
                rcvCart.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Cart.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Cart().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}