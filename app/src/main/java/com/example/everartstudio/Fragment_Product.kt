package com.example.everartstudio

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_view__product.*
import kotlinx.android.synthetic.main.fragment__product.*
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Product.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Product : Fragment() {
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
        return inflater.inflate(R.layout.fragment__product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun getData(tp:String){
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Product")
            var arlst:ArrayList<AddProductDataClass> = arrayListOf<AddProductDataClass>()
            var display:ArrayList<AddProductDataClass> = arrayListOf<AddProductDataClass>()

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    arlst.clear()
                    for(p in dataSnapshot.children) {

                        if(tp=="All"){
                           var value = p.getValue(AddProductDataClass::class.java)
                            if (value != null) {
                                arlst.add(value)

                            }

                        }
                        else{
                            if(p.child("category").value == tp){
                                var value = p.getValue(AddProductDataClass::class.java)
                                if (value != null) {
                                    arlst.add(value)

                                }
                            }
                        }

                    }
                    display.clear()
                    display.addAll(arlst)

                    var adapter = UserViewProductMainClass(context!!,display)
                    rcvProduct.adapter =adapter
                    rcvProduct.layoutManager = GridLayoutManager(context,2)
                    srvwProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (newText !!.isNotEmpty()){
                                display.clear()
                                var sr = newText.toLowerCase(Locale.getDefault())
                                arlst.forEach{
                                    if(it.product.toLowerCase(Locale.getDefault()).contains(sr)){
                                        display.add(it)
                                    }
                                }
                                rcvProduct.adapter!!.notifyDataSetChanged()
                            }
                            else{
                                display.clear()
                                display.addAll(arlst)
                                rcvProduct.adapter!!.notifyDataSetChanged()
                            }
                            return true
                        }

                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException())
                }
            })

        }
        var sp= context!!.getSharedPreferences("ProductSp",Activity.MODE_PRIVATE)
        var type= sp.getString("type","null")
        Toast.makeText(context,type.toString(),Toast.LENGTH_LONG).show()
        getData(type.toString())

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Product.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Product().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}