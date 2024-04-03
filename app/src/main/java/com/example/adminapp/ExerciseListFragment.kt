package com.example.adminapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.adminapp.databinding.FragmentExerciseListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExerciseListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExerciseListFragment : Fragment(), ClickInterface {
    lateinit var binding : FragmentExerciseListBinding
    lateinit var mainActivity: MainActivity
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var difficultyLevel: Int = 1
    private var exerciseType: Int = 1
    var exerciseList = ArrayList<ExerciseModel>()
    var db =Firebase.firestore
    var exerciseModel = ExerciseModel()
    var dayId = ""
    lateinit var exerciseAdapter: ExerciseAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {

            difficultyLevel = arguments?.getInt("difficultyLevel", 1) ?: 1
            dayId = it.getString("dayModel").toString()
            println("Check the Level: $difficultyLevel  ,$dayId")

        }

        db.collection("exercise").whereEqualTo("difficultLevel",difficultyLevel)
            .whereEqualTo("dayId",dayId)
            .addSnapshotListener { value, error ->
                if(error!=null){
                    return@addSnapshotListener
                }

                for(snapshot in value!!.documentChanges){
                    when(snapshot.type){
                        DocumentChange.Type.ADDED->{
                             exerciseModel = snapshot.document.toObject(ExerciseModel::class.java)
                            exerciseModel.id = snapshot.document.id
                            exerciseList.add(exerciseModel)
                            exerciseAdapter.notifyDataSetChanged()

                        }
                        DocumentChange.Type.MODIFIED->{
                             exerciseModel = snapshot.document.toObject(ExerciseModel::class.java)
                            exerciseModel.id = snapshot.document.id
                           var index =  exerciseList.indexOfFirst { element-> element.id == snapshot.document.id }
                            exerciseList.set(index,exerciseModel)
                            exerciseAdapter.notifyDataSetChanged()
                        }
                        DocumentChange.Type.REMOVED->{
                             exerciseModel = snapshot.document.toObject(ExerciseModel::class.java)
                            exerciseList.remove(exerciseModel)
                            exerciseAdapter.notifyDataSetChanged()
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
        binding = FragmentExerciseListBinding.inflate(layoutInflater)
        exerciseAdapter = ExerciseAdapter(mainActivity,exerciseList, this)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.rvList.layoutManager = linearLayoutManager
        binding.rvList.adapter = exerciseAdapter
        binding.fabBtn.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt("difficultyLevel",difficultyLevel)
            bundle.putString("dayId",dayId)
            mainActivity.navController.navigate(R.id.addUpdateExerciseFragment, bundle)
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
         * @return A new instance of fragment ExerciseListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(exerciseType:Int, type: Int) =
            ExerciseListFragment().apply {
                arguments = Bundle().apply {
                    putInt("type",exerciseType)
                    putInt("exerciseType",type)
                }
            }
    }

    override fun onExerciseClick(exerciseModel: ExerciseModel) {
        var bundle = Bundle()
        bundle.putSerializable("exerciseModel",exerciseModel)
        mainActivity.navController.navigate(R.id.addUpdateExerciseFragment, bundle)
    }

    override fun onExerciseViewClick(exerciseModel: ExerciseModel) {
        var bundle = Bundle()
        bundle.putSerializable("exerciseModel",exerciseModel)
        mainActivity.navController.navigate(R.id.subExerciseFragment, bundle)
    }
}