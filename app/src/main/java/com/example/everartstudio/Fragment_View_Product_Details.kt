package com.example.everartstudio

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.android.synthetic.main.fragment__view__product__details.*
import kotlinx.android.synthetic.main.view_product_custom_user.txtProductName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var sp2=context!!.getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var user = sp2.getString("uid","null")

        var sp = context!!.getSharedPreferences("ViewDetail",Activity.MODE_PRIVATE)
        var product = sp.getString("product","null")
        //Toast.makeText(context,product.toString(),Toast.LENGTH_LONG).show()

        /*ratingBar2.setOnRatingBarChangeListener(){
            ratingBar, rating, fromUser ->

            Toast.makeText(context,rating.toString(),Toast.LENGTH_LONG).show()
        }

         */

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Product").child(product.toString())
        var myRef3 = database.getReference("Wishlist")
        fun product_value(v:Int){
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
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
                    if(dataSnapshot.child("status").value.toString() == "off"){
                        btnCart.isEnabled =false
                        Toast.makeText(context,"This product is out of stock",Toast.LENGTH_LONG).show()
                    }
                    var sp3 = context!!.getSharedPreferences("img",Activity.MODE_PRIVATE)
                    var edt = sp3.edit()
                    edt.putString("url",dataSnapshot.child("img").value.toString())
                    edt.apply()
                    edt.commit()
                    if(v==1){
                        var pro = AddProductDataClass(dataSnapshot.child("product").value.toString(),dataSnapshot.child("price").value.toString().toInt(),dataSnapshot.child("detail").value.toString(),dataSnapshot.child("category").value.toString(),dataSnapshot.child("img").value.toString(),"on")
                        myRef3.child(user.toString()).child(product.toString()).setValue(pro).addOnSuccessListener {
                            Toast.makeText(context,"Successfully Added In Whishlist !!",Toast.LENGTH_LONG)
                        }.addOnFailureListener {
                            Toast.makeText(context,"Something Wrong...",Toast.LENGTH_LONG)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
        product_value(0)

        var myRef2 = database.getReference("Ratings").child(product.toString())
        var c=0.0
        var r=0.0
        myRef2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               c=0.0
                r=0.0
                for (s1 in dataSnapshot.children){
                    c+=1.0
                    r+=s1.child("star").value.toString().toFloat()
                }
                if(r>0){
                    var rs= r/c
                    ratingBar2.rating = rs.toFloat()
                }
                //val value =
                  //  dataSnapshot.getValue(String::class.java)!!
                //Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException())
            }
        })



        btnCart.setOnClickListener {
            var sp4 = context!!.getSharedPreferences("img",Activity.MODE_PRIVATE)
            var url = sp4.getString("url",null)
            var myRef2 = database.getReference("Cart")
            var cart = CartDataClass(txtProductName.text.toString(),txtProductPrice2.text.toString().toInt(),txtProductDescription.text.toString(),txtPrType.text.toString(),url.toString())
            myRef2.child(user.toString()).child(txtProductName.text.toString()).setValue(cart).addOnSuccessListener {
                Toast.makeText(context,"Product Added In Cart...",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context,"Something Wrong...",Toast.LENGTH_LONG).show()
            }
        }
        // Read from the database

        // Read from the database
        myRef3.child(user.toString()).child(product.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Toast.makeText(context,dataSnapshot.child("product").value.toString(),Toast.LENGTH_LONG).show()
                if(dataSnapshot.child("product").value.toString() == product.toString()){
                    heart_button.isLiked = true
                }else{
                    heart_button.isLiked = false
                }
                //val value =
                  //  dataSnapshot.getValue(String::class.java)!!
                //Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        heart_button.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                product_value(1)
                Toast.makeText(context,"Successfully Added In Wishlist !!",Toast.LENGTH_LONG).show()
            }
            override fun unLiked(likeButton: LikeButton) {
                myRef3.child(user.toString()).child(product.toString()).removeValue().addOnSuccessListener {
                    Toast.makeText(context,"Removed From Wishlist !!",Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(context,"something Wrong !!",Toast.LENGTH_LONG).show()
                }

            }
        })
        btncomment.setOnClickListener {
            var myRef4 = database.getReference("Users").child(user.toString())
            var myRef5 = database.getReference("Comments").child(product.toString()).push()
            val currentDateTime = LocalDateTime.now()
            var cd=currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            myRef4.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    val nm =dataSnapshot.child("name").value.toString()
                    var comments = ProductCommntsDataClass(user.toString(),nm,product.toString(),txtComment.text.toString(),cd.toString())
                    myRef5.setValue(comments).addOnSuccessListener {
                        Toast.makeText(context,"Comment Added Successfully !!",Toast.LENGTH_LONG).show()
                        txtComment.text.clear()
                    }.addOnFailureListener {
                        Toast.makeText(context,"Something Wrong !!",Toast.LENGTH_LONG).show()
                        txtComment.text.clear()
                    }
                    //Log.d(TAG, "Value is: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
        var myRef5 = database.getReference("Comments").child(product.toString())
        var cmntlst:ArrayList<ProductCommntsDataClass> = arrayListOf()
        myRef5.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                cmntlst.clear()
                for (i in dataSnapshot.children){
                    val value =i.getValue(ProductCommntsDataClass::class.java)!!
                    if(value != null){
                        
                        cmntlst.add(value)
                    }
                }
                cmntlst.reverse()
                var adapter = ProductCommentMainClass(context!!,cmntlst)
                rcv_comments.adapter =adapter
                rcv_comments.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

                //Log.d(TAG, "Value is: $value")
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