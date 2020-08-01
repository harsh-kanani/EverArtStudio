package com.example.everartstudio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.my_order_custom.view.*

class MyOrderMainClass(var ctx:Context,var arlst:ArrayList<MyOrder>):RecyclerView.Adapter<MyOrderMainClass.ViewHolder>() {
    inner class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        var pnm = v.lbl_product_nm
        var prc = v.lbl_product_prc
        var dt = v.lbl_product_dt
        var rb = v.rbOrder
        var img = v.image_my_order
        var layout = v.my_order_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(ctx)
        var View = inflater.inflate(R.layout.my_order_custom,parent,false)
        var vh = ViewHolder(View)
        return vh
    }

    override fun getItemCount(): Int {
        return arlst.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pnm.text = arlst[position].product
        holder.prc.text = "Rs."+arlst[position].price.toString()
        holder.dt.text =arlst[position].date
        holder.layout.setOnClickListener {
            Toast.makeText(ctx,arlst[position].product,Toast.LENGTH_LONG).show()
        }
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(arlst[position].img)
        storageReference.downloadUrl.addOnSuccessListener {
            val imgurl = it.toString()
            Glide.with(ctx).load(imgurl).into(holder.img).view
        }
    }
}