package com.example.express

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.express.adapter.PostViewAdapter
import com.example.express.adapter.SuggViewAdapter
import com.example.express.data.PostShow
import com.example.express.data.SuggAccount
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : Fragment() {
    private lateinit var postViewAdapter: PostViewAdapter
    private lateinit var rv_post: RecyclerView
    private val listOfPost= mutableListOf<PostShow>()
    private lateinit var loader:ShimmerFrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_post=view.findViewById(R.id.rvPost)
        loader=view.findViewById(R.id.shimmer)

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfFollowingUID= snapshot.child("listofFollowing").value as MutableList<String>
                    listOfFollowingUID.add(FirebaseAuth.getInstance().uid.toString())
                    listOfFollowingUID.forEach { 
                        getTweet(it)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun getTweet(uid: String) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var postList= mutableListOf<String>()
                    snapshot.child("listofPost").value?.let {
                        postList=it as MutableList<String>
                    }
                    val pic=snapshot.child("profilPic").value.toString()
                    val email=snapshot.child("email").value.toString()
                    var name=snapshot.child("name").value.toString()
                    if(uid==FirebaseAuth.getInstance().uid.toString()){
                        name="You"
                    }
                    postList.forEach{
                        if(!it.isNullOrBlank()){
                            listOfPost.add(PostShow(pic,email,name,it,uid))
                        }
                    }
                    postViewAdapter= PostViewAdapter(listOfPost,requireContext())
                    rv_post.layoutManager=LinearLayoutManager(requireContext())
                    rv_post.adapter=postViewAdapter
                    loader.visibility=View.INVISIBLE

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }
}