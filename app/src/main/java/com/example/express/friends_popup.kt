package com.example.express

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.express.adapter.notificationAdapter
import com.example.express.data.NotificationData
import com.example.express.data.User
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class friends_popup() : Fragment(),notificationAdapter.Click {
    private lateinit var notificationAdapter: notificationAdapter
    private lateinit var rv: RecyclerView
    private val following= mutableListOf<NotificationData>()
    private lateinit var  loader: ShimmerFrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_friends_popup, container, false)
        rv=view.findViewById(R.id.notificationRV)
        loader=view.findViewById(R.id.notifivationloader)
        card()
        return view
    }
    private fun card() {
        following.clear()
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfFollowing=snapshot.child("listofFollowing").value as MutableList<String>
                    listOfFollowing.forEach {
                        FirebaseDatabase.getInstance().getReference().child("users").child(it)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user=snapshot.getValue(User::class.java)
                                    val obj=NotificationData(user?.profilPic.toString(),user?.email.toString(),user?.name.toString(),user?.uid.toString())
                                    following.add(obj)
//                                    Log.i("output",following.toString())
                                    notificationAdapter= notificationAdapter(following,requireContext(),this@friends_popup)
                                    rv.adapter=notificationAdapter
                                    rv.layoutManager= LinearLayoutManager(requireContext())
                                    loader.visibility=View.INVISIBLE
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })
                    }
                    Log.i("output",following.toString())


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    override fun onUnfollowClick(uid:String){
        unFollow(uid)
    }

    private fun unFollow(uid: String) {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list=snapshot.child("listofFollowing").value as MutableList<String>
                    list.remove(uid)
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
                        .child("listofFollowing").setValue(list)
                    card()
                    Toast.makeText(requireContext(),"Unfollowed Successfully", Toast.LENGTH_SHORT).show()


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }



}