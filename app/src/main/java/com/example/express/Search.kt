package com.example.express

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.express.adapter.SuggViewAdapter
import com.example.express.data.SuggAccount
import com.example.express.data.User
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale


class Search : Fragment() ,SuggViewAdapter.ClickListner{

    private lateinit var suggViewAdapter: SuggViewAdapter
    private lateinit var rv_sugg:RecyclerView
    private lateinit var loader:ShimmerFrameLayout
    private lateinit var search:SearchView
    private val listOfAccount= mutableListOf<SuggAccount>()
    private val listOfSearch= mutableListOf<SuggAccount>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_search, container, false)

        rv_sugg=view.findViewById(R.id.rvSugg)
        loader=view.findViewById(R.id.suggShi)
        search=view.findViewById(R.id.search)


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfFollowing= snapshot.child("listofFollowing").value as MutableList<String>
                    FirebaseDatabase.getInstance().getReference().child("users")
                        .addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(datasnapshot in snapshot.children){
                                    val user=datasnapshot.getValue(User::class.java)

                                    if(user?.uid.toString()!=FirebaseAuth.getInstance().uid.toString() && !listOfFollowing.contains(user?.uid.toString())){
                                        val s_account=SuggAccount(user?.profilPic.toString(),user?.email.toString(),user?.uid.toString())
                                        listOfAccount.add(s_account)
                                        listOfSearch.add(s_account)

                                        suggViewAdapter=SuggViewAdapter(listOfSearch,requireContext(),this@Search)
                                        rv_sugg.adapter=suggViewAdapter
                                        rv_sugg.layoutManager=GridLayoutManager(requireContext(),2)

                                        loader.visibility=View.INVISIBLE

                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        search.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(searchText: String?): Boolean {
                listOfSearch.clear()
                val text=searchText!!.toLowerCase(Locale.getDefault())
                if(text.isNotEmpty()){
                    listOfAccount.forEach {
                        if(it.sugEmail.toLowerCase(Locale.getDefault()).contains(text)){
                            listOfSearch.add(it)
                        }
                    }
                    rv_sugg.adapter!!.notifyDataSetChanged()
                }
                else{
                    listOfSearch.clear()
                    listOfSearch.addAll(listOfAccount)
                    rv_sugg.adapter!!.notifyDataSetChanged()

                }
                return false
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                listOfSearch.clear()
                val text=searchText!!.toLowerCase(Locale.getDefault())
                if(text.isNotEmpty()){
                    listOfAccount.forEach {
                        if(it.sugEmail.toLowerCase(Locale.getDefault()).contains(text)){
                            listOfSearch.add(it)
                        }
                    }
                    rv_sugg.adapter!!.notifyDataSetChanged()
                }
                else{
                    listOfSearch.clear()
                    listOfSearch.addAll(listOfAccount)
                    rv_sugg.adapter!!.notifyDataSetChanged()

                }
                return false

            }

        })

        return view
    }
    private fun follow(uid:String){
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfFollowing= snapshot.child("listofFollowing").value as MutableList<String>
                    listOfFollowing.add(uid)

                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().uid.toString())
                        .child("listofFollowing").setValue(listOfFollowing)

                    Toast.makeText(requireContext(),"Followed Successfully",Toast.LENGTH_SHORT).show()


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
    override fun onFollowClick(uid:String) {
        follow(uid)

    }


}