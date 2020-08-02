package com.example.everartstudio

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.android.synthetic.main.fragment__view__product__details.*
import kotlinx.android.synthetic.main.wishlist_custom.view.*

class WishlistMainClass(var ctx:Context,var arlst:ArrayList<AddProductDataClass>,var user:String):RecyclerView.Adapter<WishlistMainClass.ViewHolder>() {
    inner class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        var pnm = v.lbl_wishlist_pnm
        var prc= v.lbl_wishlist_prc
        var img = v.img_wishlist
        var likeButton = v.wishlistLike
        var btn = v.btn_wishlist_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(ctx)
        var View = inflater.inflate(R.layout.wishlist_custom,parent,false)
        var vh = ViewHolder(View)
        return vh
    }

    override fun getItemCount(): Int {
        return arlst.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pnm.text =arlst[position].product
        holder.prc.text ="Rs."+arlst[position].price.toString()
        holder.likeButton.isLiked = true
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(arlst[position].img)
        storageReference.downloadUrl.addOnSuccessListener {
            val imgurl = it.toString()
            Glide.with(ctx).load(imgurl).into(holder.img).view
        }
        holder.likeButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                //product_value(1)
                //Toast.makeText(context,"Successfully Added In Wishlist !!", Toast.LENGTH_LONG).show()
            }
            override fun unLiked(likeButton: LikeButton) {
                val database = FirebaseDatabase.getInstance()

                var myRef3 = database.getReference("Wishlist")
                myRef3.child(user.toString()).child(arlst[position].product).removeValue().addOnSuccessListener {
                    Toast.makeText(ctx,"Removed From Wishlist !!", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(ctx,"something Wrong !!", Toast.LENGTH_LONG).show()
                }
                val transaction =(ctx as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container,Fragment_Wishlist())
                transaction.addToBackStack(null)
                transaction.commit()

            }

        })
        holder.btn.setOnClickListener {
            var sp = ctx!!.getSharedPreferences("ViewDetail", Activity.MODE_PRIVATE)
            var edt = sp.edit()
            edt.putString("product",arlst[position].product.toString())
            edt.apply()
            edt.commit()
            val transaction =(ctx as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,Fragment_View_Product_Details())
            transaction.addToBackStack(null)
            transaction.commit()

        }


    }
}