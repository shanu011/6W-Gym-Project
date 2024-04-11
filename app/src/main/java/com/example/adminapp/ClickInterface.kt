package com.example.adminapp

interface ClickInterface {
    fun onExerciseClick(exerciseModel : ExerciseModel)
    fun onExerciseViewClick(exerciseModel : ExerciseModel)

}
interface DayClickInterface {
    fun onDayClick(dayModel : DayModel)

    fun onEdit(dayModel: DayModel)

    fun onDelete(dayModel: DayModel)

}