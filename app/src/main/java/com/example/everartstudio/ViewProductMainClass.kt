package com.example.everartstudio

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.view_product_custom.view.*

class ViewProductMainClass(var ctx:Activity,var arlst:ArrayList<AddProductDataClass>):RecyclerView.Adapter<ViewProductMainClass.ViewHolder>() {
    inner class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        var pimg = v.prod_img
        var pnm = v.prod_name
        var prc = v.prod_price
        var del = v.btn_del
        var edt = v.btn_edit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =ctx.layoutInflater.inflate(R.layout.view_product_custom,parent,false)
        var vh= ViewHolder(view)
        return  vh
    }

    override fun getItemCount(): Int {
        return arlst.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pnm.text =arlst[position].product
        holder.prc.text =arlst[position].price.toString()

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(arlst[position].img)
        storageReference.downloadUrl.addOnSuccessListener {
            val imgurl = it.toString()
            Glide.with(ctx).load(imgurl).into(holder.pimg).view
        }

    }
}