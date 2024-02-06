package com.example.express.data

data class User (
    val name:String="",
    val email:String="",
    val uid:String="",
    val listofFollowing:List<String> =listOf(),
    val listofPost: List<String> =listOf(),
    val listofNotification:List<String> =listOf(),
    val profilPic:String=""
    
)