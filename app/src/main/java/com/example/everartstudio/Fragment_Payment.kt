package com.example.everartstudio

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment__payment.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Payment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Payment : Fragment() {
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
        return inflater.inflate(R.layout.fragment__payment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnCheckOut.setOnClickListener {
            var p=""
            if(rdcash.isChecked){
                p="cash"
            }
            if(rdwallet.isChecked){
                p="wallet"
            }
            var sp = context!!.getSharedPreferences("MySp",Activity.MODE_PRIVATE)
            var user = sp.getString("uid","null")
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Cart")
            // Read from the database

            // Read from the database
            val currentDateTime = LocalDateTime.now()
            var cd=currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(i in dataSnapshot.child(user.toString()).children){
                        val value =i.getValue(AddProductDataClass::class.java)!!
                        Log.d("order","value is : $value")
                        var value2=dataSnapshot.child("Address").child(user.toString()).getValue(AddressDataClass::class.java)!!
                        Log.d("order","Address is $value2")


                        var myRef2 = database.getReference().child("Myorder").child(user.toString()).push()
                        var or = MyOrder(value.product,value.price,value.detail,value.category,value.img,value2.name,value2.mobile,value2.state,value2.city,value2.pincode,value2.address,p,cd.toString(),user.toString(),"pending",myRef2.key.toString())
                        //Toast.makeText(context,myRef2.key.toString(),Toast.LENGTH_LONG).show()
                        myRef2.setValue(or).addOnSuccessListener {
                            Toast.makeText(context,"Your Order Is Received !!", Toast.LENGTH_LONG).show()
                            var fragmentManager: FragmentManager = activity!!.supportFragmentManager
                            var fragmentTransaction: FragmentTransaction =  fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.fragment_container,Fragment_Thank_You())
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }.addOnFailureListener {
                            Toast.makeText(context,"Something Wronng !!", Toast.LENGTH_LONG).show()
                        }

                        //var myRef1=database.getReference().child("check").push()
                        //myRef1.child("hello").setValue("hi")
                    }

                    //Log.d(TAG, "Value is: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
            //Toast.makeText(context,p, Toast.LENGTH_LONG).show()


        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Payment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Payment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}