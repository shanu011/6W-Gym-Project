package com.example.adminapp

interface ClickInterface {
    fun onExerciseClick(exerciseModel : ExerciseModel)
    fun onExerciseViewClick(exerciseModel : ExerciseModel)

}
interface DayClickInterface {
    fun onDayClick(dayModel : DayModel)
<<<<<<< Updated upstream
     fun onEdit(dayModel: DayModel)
=======
    fun onEdit(dayModel: DayModel)

>>>>>>> Stashed changes
    fun onDelete(dayModel: DayModel)

}