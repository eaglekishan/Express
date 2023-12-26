package com.example.express.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.express.R
import com.example.express.data.PostShow

class PostViewAdapter(private val postcard:List<PostShow>, private val context: Context,):RecyclerView.Adapter<PostViewAdapter.ViewHolder>() {

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val postname: TextView =itemView.findViewById(R.id.postname)
        val postpic:ImageView=itemView.findViewById(R.id.postPic)
        val postemail:TextView=itemView.findViewById(R.id.postemail)
        val postContent:TextView=itemView.findViewById(R.id.postcontent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.postcard,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return postcard.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=postcard[position]
        holder.postContent.text=item.postContent
        holder.postemail.text=item.postEmail
        holder.postname.text=item.postName
        if(item.postProfile.length!=0){
            Glide.with(context)
                .load(item.postProfile)
                .into(holder.postpic)
        }
    }
}