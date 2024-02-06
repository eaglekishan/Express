package com.example.express

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.express.adapter.PostViewAdapter
import com.example.express.adapter.PostViewAdapterProfile
import com.example.express.data.PostShow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.UUID



class Profile : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private lateinit var ppic:ImageView
    private lateinit var pName:TextView
    private lateinit var pEmail:TextView
    private lateinit var postViewAdapter: PostViewAdapterProfile
    private lateinit var rv_profile: RecyclerView
    private lateinit var postno:TextView
    private lateinit var connectionno:TextView
    private val listOfPost= mutableListOf<PostShow>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)
        init(view)

        showDetails()

        ppic.setOnClickListener{
            val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,101)

        }
        return view
    }

    private fun showDetails() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email=snapshot.child("email").getValue().toString()
                    val name=snapshot.child("name").getValue().toString()
                    val pic=snapshot.child("profilPic").getValue().toString()
                    pName.text=name
                    pEmail.text=email
                    if(pic.length!=0){
                        Glide.with(this@Profile)
                            .load(pic)
                            .into(ppic)
                    }

                    var postList= mutableListOf<String>()
                    snapshot.child("listofPost").value?.let {
                        postList=it as MutableList<String>
                    }
                    postno.text=(postList.size-1).toString()
                    var connectionList= mutableListOf<String>()
                    snapshot.child("listofFollowing").value?.let {
                        connectionList=it as MutableList<String>
                    }
                    connectionno.text=(connectionList.size).toString()
                    postList.forEach{
                        if(!it.isNullOrBlank()){
                            listOfPost.add(PostShow(pic,"","You",it,FirebaseAuth.getInstance().uid.toString()))
                        }
                    }
                    postViewAdapter= PostViewAdapterProfile(listOfPost,requireContext())
//                    rv_profile.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    rv_profile.layoutManager=GridLayoutManager(requireContext(),1)
                    rv_profile.adapter=postViewAdapter




                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101 && resultCode==RESULT_OK){
            upload(data?.data)
        }
    }

    private fun upload(data: Uri?) {
        val filename= UUID.randomUUID().toString()+".jpg"
        val storageRef=FirebaseStorage.getInstance().reference.child("profilePic/$filename")
        storageRef.putFile(data!!).addOnSuccessListener {
            val url=  it.metadata?.reference?.downloadUrl
            url?.addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
                    .child("profilPic").setValue(it.toString())
            }
        }
        ppic.setImageURI(data)


    }

    private fun init(view: View) {
        ppic=view.findViewById(R.id.ppic)
        pName=view.findViewById(R.id.pName)
        pEmail=view.findViewById(R.id.pEmail)
        rv_profile=view.findViewById(R.id.rv_profile)
        postno=view.findViewById(R.id.noofpost)
        connectionno=view.findViewById(R.id.noofFriends)

    }


}