package com.example.everartstudio

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment__my_order__view__deatils.*
import kotlinx.android.synthetic.main.fragment__view__product__details.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_MyOrder_View_Deatils.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_MyOrder_View_Deatils : Fragment() {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var sp = context!!.getSharedPreferences("Order", Activity.MODE_PRIVATE)
        var oid = sp.getString("oid","null")

        var sp2 = context!!.getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var user = sp2.getString("uid","null")

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Myorder").child(user.toString()).child(oid.toString())

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                lbl_Oid.text ="Order Id : "+ dataSnapshot.child("oid").value.toString()
                lbl_Oreder_product.text = dataSnapshot.child("product").value.toString()
                lbl_Order_Prc.text ="Rs."+ dataSnapshot.child("price").value.toString()
                lbl_mono.text = dataSnapshot.child("mobile").value.toString()
                lbl_order_add.text = dataSnapshot.child("address").value.toString()
                lbl_odt.text = "Date : "+dataSnapshot.child("date").value.toString()
                lbl_odSatus.text = "Status : "+dataSnapshot.child("status").value.toString()
                val storage = FirebaseStorage.getInstance()
                val storageReference = storage.getReferenceFromUrl(dataSnapshot.child("img").value.toString())
                storageReference.downloadUrl.addOnSuccessListener {
                    val imgurl = it.toString()
                    Glide.with(activity!!).load(imgurl).into(img_ordr).view
                }

                //Log.d("Value",value.toString())

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        btn_order_ok.setOnClickListener {
            var fragmentManager:FragmentManager = activity!!.supportFragmentManager
            var fragmentTransaction:FragmentTransaction =  fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,Fragment_MyOrder())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__my_order__view__deatils, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_MyOrder_View_Deatils.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_MyOrder_View_Deatils().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}