package com.example.express

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class Post : Fragment() {
    private lateinit var postEdt:EditText
    private lateinit var postBtn:Button
    private lateinit var postpic:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_post, container, false)
        init(view)
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email=snapshot.child("email").getValue().toString()
                    val name=snapshot.child("name").getValue().toString()
                    val pic=snapshot.child("profilPic").getValue().toString()

                    if(pic.length!=0){
                        Glide.with(this@Post)
                            .load(pic)
                            .into(postpic)
                    }




                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        postBtn.setOnClickListener {
            val a=postEdt.text.toString()
//            Toast.makeText(activity,a,Toast.LENGTH_SHORT).show()


            post(a)

        }

        return view
    }

    private fun post(a: String) {

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listofpost=snapshot.child("listofPost").value as MutableList<String>
                    listofpost.add(a)
                    uploadPost(listofpost)
                    

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
    private fun uploadPost(listOfPost:List<String>){
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .child("listofPost").setValue(listOfPost)
        postEdt.text.clear()
        Toast.makeText(activity,"Post Uploded Successfully",Toast.LENGTH_SHORT).show()
    }

    private fun init(view: View){
        postBtn= view.findViewById(R.id.postbtn)
        postEdt = view.findViewById(R.id.postedt)
        postpic=view.findViewById(R.id.postphoto)
    }

}