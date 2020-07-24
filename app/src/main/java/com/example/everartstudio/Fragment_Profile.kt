package com.example.everartstudio

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment__profile.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Profile : Fragment() {
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
        return inflater.inflate(R.layout.fragment__profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var sp=context!!.getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var user = sp.getString("uid","null")
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Users").child(user.toString())

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                txt_profile_mobile.setText(dataSnapshot.child("mobile").value.toString())
                txt_profile_name.setText(dataSnapshot.child("name").value.toString())
                txt_profile_pass.setText(dataSnapshot.child("password").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        btn_save.setOnClickListener {
            myRef.child("mobile").setValue(txt_profile_mobile.text.toString())
            myRef.child("name").setValue(txt_profile_name.text.toString())
            myRef.child("password").setValue(txt_profile_pass.text.toString())
            Toast.makeText(context,"Successfully Change Profile details...",Toast.LENGTH_LONG).show()

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}