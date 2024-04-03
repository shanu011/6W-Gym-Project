package com.example.adminapp

import java.io.Serializable

data class ExerciseModel(
    var id: String? = null,
    var image: String? = null,
    var imageList: ArrayList<String>? = null,
    var exerciseName: String? = null,
    var exerciseTypeName: String? = null,
    var exerciseDescription: String? = null,
    var difficultLevel: Int? =0,
    var exerciseType: Int? =0,
    var dayId: String ? = null,
    var WeigthGainOrLoss: Int? = 0
): Serializable
