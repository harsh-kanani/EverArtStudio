package com.example.everartstudio

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment__address.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Address.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Address : Fragment() {
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
        return inflater.inflate(R.layout.fragment__address, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var sp=context!!.getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var uid = sp.getString("uid","null")
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Cart").child("Address")
        btnAddAddress.setOnClickListener {
            var add= AddressDataClass(txtAddName.text.toString(),txtAddMobile.text.toString(),txtAddState.text.toString(),txtAddCity.text.toString(),txtAddPinCode.text.toString(),txtAddAddress.text.toString())
            myRef.child(uid.toString()).setValue(add).addOnSuccessListener {
                Toast.makeText(context,"Successfully Add !!",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context,"Something Problem...",Toast.LENGTH_LONG).show()
            }
            var fragmentManager: FragmentManager = activity!!.supportFragmentManager
            var fragmentTransaction: FragmentTransaction =  fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container,Fragment_Payment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Address.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Address().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}