package com.example.everartstudio

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.conform_order_custom.view.*

class ConformOrderMainClass(var ctx:Activity,var arlst:ArrayList<MyOrder>):RecyclerView.Adapter<ConformOrderMainClass.ViewHolder>() {
    inner class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        var prd = v.lbl_confrm_prd
        var prc = v.lbl_cnfrm_prc
        var oid = v.lbl_cnfrm_oid
        var dt = v.lbl_cnfrm_dt
        var layout = v.layout_conform_order
        var img = v.img_conform
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var View = ctx.layoutInflater.inflate(R.layout.conform_order_custom,parent,false)
        var vh = ViewHolder(View)
        return vh
    }

    override fun getItemCount(): Int {
        return arlst.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.prd.text = arlst[position].product
        holder.prc.text = arlst[position].price.toString()
        holder.oid.text = "Order Id : "+arlst[position].oid
        holder.dt.text = "Date : "+arlst[position].date

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(arlst[position].img)
        storageReference.downloadUrl.addOnSuccessListener {
            val imgurl = it.toString()
            Glide.with(ctx).load(imgurl).into(holder.img).view
        }

        holder.layout.setOnClickListener {
            var dialog = Dialog(ctx)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.order_custom_dialog)
            var unm = dialog.findViewById<TextView>(R.id.lbl_view_nm)
            var mob = dialog.findViewById<TextView>(R.id.lbl_view_mob)
            var st = dialog.findViewById<TextView>(R.id.lbl_view_st)
            var ct = dialog.findViewById<TextView>(R.id.lbl_view_ct)
            var pin = dialog.findViewById<TextView>(R.id.lbl_view_pin)
            var add = dialog.findViewById<TextView>(R.id.lbl_view_Addr)
            var btn = dialog.findViewById<Button>(R.id.btn_view_ok)
            unm.text ="Name : "+ arlst[position].name
            mob.text = "Mobile : "+arlst[position].mobile
            st.text = "State : "+arlst[position].state
            ct.text = "City : "+arlst[position].city
            pin.text = "Pincode : "+arlst[position].pincode
            add.text = "Address : "+arlst[position].address
            btn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}