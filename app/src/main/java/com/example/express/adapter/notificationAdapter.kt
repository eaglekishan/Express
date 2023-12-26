package com.example.express.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.express.R
import com.example.express.data.NotificationData

class notificationAdapter(private val following:List<NotificationData>
,private val context:Context,
    private val click:Click):RecyclerView.Adapter<notificationAdapter.ViewHolder> (){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val email=itemView.findViewById<TextView>(R.id.notificationEmail)
        val name:TextView=itemView.findViewById(R.id.notificationName)
        val btn:Button=itemView.findViewById(R.id.notificationBtn)
        val pic:ImageView=itemView.findViewById(R.id.notificationpic)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.notificationview,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return following.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=following[position]
        if (item.Profile.isNotEmpty()){
            Glide.with(context).load(item.Profile).into(holder.pic)
        }
        holder.email.text=item.Email
        holder.name.text=item.Name

        holder.btn.setOnClickListener {
            click.onUnfollowClick(item.uid)
            holder.btn.text="Follow"
        }

    }
    interface Click{
        fun onUnfollowClick(uid:String)

    }
}