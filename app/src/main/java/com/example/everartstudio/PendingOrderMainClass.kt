package com.example.everartstudio

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.pending_order_custom.view.*


class PendingOrderMainClass(var ctx:Activity,var arlst:ArrayList<MyOrder>):RecyclerView.Adapter<PendingOrderMainClass.ViewHolder>() {
    inner class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        var product = v.lbl_pending_product
        var price = v.lbl_pending_prc
        var dt = v.lbl_pending_date
        var oid = v.lbl_pending_oid
        var cnf = v.btn_od_conform
        var can = v.btn_od_cancel
        var img = v.img_pending
        var layout = v.layout_pending_order
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var View = ctx.layoutInflater.inflate(R.layout.pending_order_custom,parent,false)
        var vh = ViewHolder(View)
        return vh
    }

    override fun getItemCount(): Int {
        return arlst.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.product.text =arlst[position].product
        holder.price.text ="Rs."+ arlst[position].price.toString()
        holder.oid.text = "Order id:"+arlst[position].oid
        holder.dt.text = "Date: "+arlst[position].date

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(arlst[position].img)
        storageReference.downloadUrl.addOnSuccessListener {
            val imgurl = it.toString()
            Glide.with(ctx).load(imgurl).into(holder.img).view
        }
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Myorder").child(arlst[position].uid).child(arlst[position].oid)
        fun status(st:String){
            myRef.child("status").setValue(st).addOnSuccessListener {
                Toast.makeText(ctx,"Order Is $st !!",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(ctx,"Something Wrong !!",Toast.LENGTH_LONG).show()
            }
        }
        holder.cnf.setOnClickListener {
            status("conform")
        }
        holder.can.setOnClickListener {
            status("cancelled")
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