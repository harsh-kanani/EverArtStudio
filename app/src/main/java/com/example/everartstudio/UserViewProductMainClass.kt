package com.example.everartstudio

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.view_product_custom_user.view.*

class UserViewProductMainClass(var ctx:Context,var arlst:ArrayList<AddProductDataClass>):RecyclerView.Adapter<UserViewProductMainClass.ViewHolder>() {
    inner class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        var img = v.imgProductImage
        var nm = v.txtProductName
        var prc = v.txtProductPrice
        var btn = v.btnViewDetail


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(ctx)
        var view =inflater.inflate(R.layout.view_product_custom_user,parent,false)
        var vh = ViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int {
        return arlst.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nm.text = arlst[position].product
        holder.prc.text = "Rs."+arlst[position].price.toString()

        holder.btn.setOnClickListener {
            var sp = ctx.getSharedPreferences("ViewDetail",Activity.MODE_PRIVATE)
            var edt = sp.edit()
            edt.putString("product",arlst[position].product)
            edt.apply()
            edt.commit()
            /*
            var fragmentManager: FragmentManager = activity!!.supportFragmentManager
            var fragmentTransaction: FragmentTransaction =  fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,Fragment_Product())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

             */
        }


        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(arlst[position].img)
        storageReference.downloadUrl.addOnSuccessListener {
            val imgurl = it.toString()
            Glide.with(ctx).load(imgurl).into(holder.img).view
        }

    }
}