package com.example.adminapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.adminapp.databinding.LevelFragmentBinding

class LevelFragment: Fragment() {
    lateinit var binding : LevelFragmentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       mainActivity = activity as MainActivity
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LevelFragmentBinding.inflate(layoutInflater)
        binding.btnBeginners.setOnClickListener {
            mainActivity.navController.navigate(R.id.dayWiseExerciseFragment, bundleOf("difficultyLevel" to 1))
        }
        binding.btnIntermidiate.setOnClickListener {
            mainActivity.navController.navigate(R.id.dayWiseExerciseFragment, bundleOf("difficultyLevel" to 2))
        }
        binding.btnAdvance.setOnClickListener {
            mainActivity.navController.navigate(R.id.dayWiseExerciseFragment, bundleOf("difficultyLevel" to 3))
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
         * @return A new instance of fragment AllExerciseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllExerciseFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}