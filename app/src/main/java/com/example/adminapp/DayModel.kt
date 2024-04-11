package com.example.adminapp

import java.io.Serializable

data class DayModel(
    var id: String? = null,
    var day: String? = null,
    var difficultyLevel : Int = 0,
    var exerciseType : Int = 0,
    var count : Int ? = 0
): Serializable
