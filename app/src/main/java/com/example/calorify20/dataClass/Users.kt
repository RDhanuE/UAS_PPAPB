package com.example.calorify20.dataClass

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import java.util.Date

data class Users(

    @set:Exclude @get:Exclude @Exclude
    var id : String = "",
    var username : String = "",
    var email : String = "",
    var current_weight : Int,
    var current_height : Int,
    var goal_weight : Int,
    var goal_calories : Int,
    var goal_date : String,
    var isAdmin : Boolean
)
