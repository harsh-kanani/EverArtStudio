package com.example.everartstudio

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comments_custom.view.*

class ProductCommentMainClass(var ctx:Context,var arlst:ArrayList<ProductCommntsDataClass>):RecyclerView.Adapter<ProductCommentMainClass.ViewHolder>() {
    inner class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        var cmnt = v.lbl_comment
        var user = v.lbl_comment_unm
        var dt = v.lbl_comment_dt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(ctx)
        var View = inflater.inflate(R.layout.comments_custom,parent,false)
        var vh = ViewHolder(View)
        return vh
    }

    override fun getItemCount(): Int {
        return arlst.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cmnt.text = arlst[position].comment
        holder.user.text ="@ ${arlst[position].name}"
        holder.dt.text = arlst[position].date
    }
}