package com.example.calorify20.dataClass

import com.google.firebase.firestore.Exclude

data class Preset(
    @set:Exclude @get:Exclude @Exclude
    var id : String = "",
    var type : String = "",
    var occasion : String = "",
    var item : String = "",
)
