package com.example.adminapp

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.adminapp.databinding.DayDialogBinding
import com.example.adminapp.databinding.FragmentDayWiseExerciseBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DayWiseExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayWiseExerciseFragment : Fragment(), DayClickInterface {
    lateinit var binding: FragmentDayWiseExerciseBinding
    lateinit var dayAdapter: DayWiseAdapter
    var db = Firebase.firestore
    var dayModel = DayModel()
    lateinit var mainActivity: MainActivity
    var dayList = ArrayList<DayModel>()
    var sortDayWise = arrayListOf<DayModel>()
    private var difficultyLevel: Int = 1
    private var exerciseType: Int = 1
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            difficultyLevel = arguments?.getInt("difficultyLevel", 1) ?: 1
            println("Check the Level: $difficultyLevel")
        }
        // dayList.clear()

        db.collection("day").whereEqualTo("difficultyLevel", difficultyLevel)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                dayList.clear()
                for(snapshot in value!!.documentChanges){
                    when(snapshot.type){
                        DocumentChange.Type.ADDED->{
                            dayModel = snapshot.document.toObject(DayModel::class.java)
                            dayModel.id = snapshot.document.id
                            dayList.add(dayModel)
                            dayList.sortBy { it.day }
                            println("SortList: $dayList")
                            dayAdapter.notifyDataSetChanged()

                        }
                        DocumentChange.Type.MODIFIED->{
                            dayModel = snapshot.document.toObject(DayModel::class.java)
                            dayModel.id = snapshot.document.id
                            var index =  dayList.indexOfFirst { element-> element.id == snapshot.document.id }
                            dayList.set(index,dayModel)
                            dayList.sortBy { it.day }
                            dayAdapter.notifyDataSetChanged()
                        }
                        DocumentChange.Type.REMOVED->{
                            dayModel = snapshot.document.toObject(DayModel::class.java)
                            dayList.remove(dayModel)
                            dayList.sortBy { it.day }
                            dayAdapter.notifyDataSetChanged()
                        }
                    }
                }

            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayWiseExerciseBinding.inflate(layoutInflater)
        dayAdapter = DayWiseAdapter(dayList, this)
        binding.rvList.layoutManager = LinearLayoutManager(mainActivity)
        binding.rvList.adapter = dayAdapter
        binding.fabBtn.setOnClickListener {
            var dialog = Dialog(mainActivity)
            var dialogBinding = DayDialogBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.show()
            dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            dialogBinding.btnSave.setOnClickListener {
                if (dialogBinding.etDay.text.toString().isEmpty()) {
                    dialogBinding.etDay.error = "Enter your Day"
                } else {
                    var dayModel = DayModel()
                    dayModel.day = dialogBinding.etDay.text.toString()
                    dayModel.difficultyLevel = difficultyLevel
                    db.collection("day").add(dayModel).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(mainActivity, "saved", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DayWiseExerciseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(exerciseType: Int, type: Int) =
            DayWiseExerciseFragment().apply {
                arguments = Bundle().apply {
                    putInt("type", exerciseType)
                    putInt("exerciseType", type)
                }
            }
    }

    override fun onDayClick(dayModel: DayModel) {
        var bundle = Bundle()
        bundle.putString("dayModel", dayModel.id)
        bundle.putInt("difficultyLevel", difficultyLevel)
        System.out.println("DayModelId: ${dayModel.id}")
        mainActivity.navController.navigate(R.id.exerciseListFragment, bundle)
    }

         override fun onEdit(dayModel: DayModel) {
             var dialog = Dialog(mainActivity)
             var dialogBinding = DayDialogBinding.inflate(layoutInflater)
             dialog.setContentView(dialogBinding.root)
             dialog.show()
             dialogBinding.btnSave.setText("Update")
             dialogBinding.etDay.setText(dayModel.day)
             dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
             dialogBinding.btnSave.setOnClickListener {
               if (dialogBinding.etDay.text.toString().isEmpty()) {
                dialogBinding.etDay.error = "Enter your Day"
                } else {
                   dayModel.day = dialogBinding.etDay.text.toString()
                   dayModel.difficultyLevel = difficultyLevel

                    db.collection("day").document(dayModel.id.toString()).set(dayModel)
                          .addOnCompleteListener {
                           if (it.isSuccessful) {
                               Toast.makeText(mainActivity, "Update", Toast.LENGTH_SHORT).show()
                               dialog.dismiss()
                        }
                    }
            }

        }
        dialog.show()


    }

    override fun onDelete(dayModel: DayModel) {
        db.collection("day").document(dayModel.id.toString()).delete().addOnCompleteListener {

            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Delete", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Not Delete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

