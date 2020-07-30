package com.example.everartstudio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.cart_custom.view.*


class CartMainClass(var ctx:Context , var arlst:ArrayList<CartDataClass>,var uid:String): RecyclerView.Adapter<CartMainClass.ViewHolder>(){
    inner class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        var pnm = v.lblProductName
        var prc = v.lblProductPrice
        var detail = v.lblProductDes
        var type = v.lblProductCategory
        var img = v.imageViewProduct
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(ctx)
        var View = inflater.inflate(R.layout.cart_custom,parent,false)
        var vh = ViewHolder(View)
        return vh
    }

    override fun getItemCount(): Int {
        return arlst.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pnm.text = arlst[position].product
        holder.prc.text = "Rs."+arlst[position].price.toString()
        holder.detail.text = arlst[position].detail
        holder.type.text = arlst[position].category
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(arlst[position].img)
        storageReference.downloadUrl.addOnSuccessListener {
            val imgurl = it.toString()
            Glide.with(ctx).load(imgurl).into(holder.img).view
        }
    }
}