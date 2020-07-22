package com.example.everartstudio

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
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
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Product")

        holder.del.setOnClickListener {
            myRef.child(arlst[position].product).removeValue().addOnSuccessListener {
                Toast.makeText(ctx,"Successfully Deleted...",Toast.LENGTH_LONG).show()

            }.addOnFailureListener {
                Toast.makeText(ctx,"Something Problem  !!",Toast.LENGTH_LONG).show()
            }
        }
        holder.edt.setOnClickListener {
            var dialog = Dialog(ctx)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.edit_product_custom_dialog)
            var change=dialog.findViewById<Button>(R.id.btn_price_edt)
            var cancel=dialog.findViewById<Button>(R.id.btn_dlg_cancel)
            var price = dialog.findViewById<EditText>(R.id.txt_new_price)
            change.setOnClickListener {
                myRef.child(arlst[position].product).child("price").setValue(price.text.toString().toInt()).addOnSuccessListener {
                    Toast.makeText(ctx,"Successfully Change Price...",Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(ctx,"Something Problem  !!",Toast.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }



    }
}