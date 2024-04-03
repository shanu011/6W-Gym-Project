package com.example.adminapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adminapp.databinding.FragmentAllExerciseBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllExerciseFragment : Fragment() {
    lateinit var binding : FragmentAllExerciseBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewPagerAdapter: ViewPagerAdapter
    var titles = arrayListOf("Beginners", "Intermediate", "Advanced")
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var type = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            type = it.getInt("type")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllExerciseBinding.inflate(layoutInflater)
      //  mainActivity.supportActionBar.apply { binding.toolbar }
//        var fragments = arrayListOf<Fragment>(
//            DayWiseExerciseFragment.newInstance(1,type),
//            DayWiseExerciseFragment.newInstance(2,type),
//            DayWiseExerciseFragment.newInstance(3,type)
//        )
//        viewPagerAdapter = ViewPagerAdapter(mainActivity, fragments)
//        binding.viewPager.adapter = viewPagerAdapter
//
//        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
//            tab.text = titles[position]
//        }.attach()
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}