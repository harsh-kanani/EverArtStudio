package com.example.everartstudio

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment__view__product__details.*
import kotlinx.android.synthetic.main.view_product_custom_user.*
import kotlinx.android.synthetic.main.view_product_custom_user.txtProductName


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_View_Product_Details.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_View_Product_Details : Fragment() {
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
        return inflater.inflate(R.layout.fragment__view__product__details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var sp = context!!.getSharedPreferences("ViewDetail",Activity.MODE_PRIVATE)
        var product = sp.getString("product","null")
        //Toast.makeText(context,product.toString(),Toast.LENGTH_LONG).show()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Product").child(product.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                txtProductName.text = dataSnapshot.child("product").value.toString()
                txtProductPrice2.text = dataSnapshot.child("price").value.toString()

                val storage = FirebaseStorage.getInstance()
                val storageReference = storage.getReferenceFromUrl(dataSnapshot.child("img").value.toString())
                storageReference.downloadUrl.addOnSuccessListener {
                    val imgurl = it.toString()
                    Glide.with(activity!!).load(imgurl).into(imgProductView).view
                }
                txtProductDescription.text = dataSnapshot.child("detail").value.toString()
                txtPrType.text = dataSnapshot.child("category").value.toString()


                //Log.d("check", dataSnapshot.child("product").value.toString())
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
         * @return A new instance of fragment Fragment_View_Product_Details.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_View_Product_Details().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}