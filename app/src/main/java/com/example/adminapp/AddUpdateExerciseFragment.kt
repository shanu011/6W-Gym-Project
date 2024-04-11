package com.example.adminapp

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.example.adminapp.databinding.FragmentAddUpdateExerciseBinding



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddUpdateExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddUpdateExerciseFragment : Fragment() {
    lateinit var binding: FragmentAddUpdateExerciseBinding
    lateinit var mainActivity: MainActivity
    var storageRef = FirebaseStorage.getInstance().reference
    var db = Firebase.firestore
    lateinit var imageUri: Uri
    lateinit var imageAdapter : ImageAdapter
    lateinit var arrayAdapter: ArrayAdapter<String>
    var array = arrayOf("Chest","Biceps","Triceps","Legs")
    var weightGainArray = arrayOf("Select Ctageory", "Weight Gain","Weight Loss")
    // TODO: Rename and change types of parameters
    var imageList = arrayListOf<Uri>()
    var isUpdate = false
    var exerciseName= ""
    var exerciseType = 0
    var difficultyLevel = 1
    var exerciseModel = ExerciseModel()
    var exerciseId = ""
    var dayId = ""
    var weightGORL = 0
    var permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){

            println("Permission Granted")
        }else{
            println("Permission Not Granted")
        }
    }
    val imagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {uris->
            uris?.let {
              //  binding.ivExerciseImageList.setImageURI(it)
                imageUri = it
                Glide.with(this).load(it).into(binding.ivExerciseImageList)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            arguments?.let {
                difficultyLevel = arguments?.getInt("difficultyLevel", 1) ?: 1
                dayId = it.getString("dayId").toString()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddUpdateExerciseBinding.inflate(layoutInflater)

        arrayAdapter = ArrayAdapter(mainActivity,android.R.layout.simple_list_item_1,array)
        binding.spinner.adapter = arrayAdapter
        arrayAdapter = ArrayAdapter(mainActivity,android.R.layout.simple_list_item_1,weightGainArray)
        binding.weightGainSpinner.adapter = arrayAdapter

     //   imageAdapter = ImageAdapter(imageList)
//        binding.ivExerciseImageList.layoutManager = LinearLayoutManager(mainActivity,LinearLayoutManager.HORIZONTAL,false)
//        binding.ivExerciseImageList.adapter = imageAdapter

        binding.spinner.onItemSelectedListener = object :OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                exerciseName = array[position]
                exerciseType = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

                binding.weightGainSpinner.onItemSelectedListener = object :OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                       // exerciseName = weightGainArray[position]
                        if(position!=0){
                            weightGORL = position
                        }else{
                            Toast.makeText(mainActivity,"Select Category",Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

        arguments?.let {
            if (it.containsKey("exerciseModel")) {
                exerciseModel = it.getSerializable("exerciseModel") as ExerciseModel
                binding.etExerciseName.setText(exerciseModel.exerciseName)
                binding.etExerciseDescription.setText(exerciseModel.exerciseDescription)
                exerciseId = exerciseModel.id.toString()
                Glide.with(mainActivity)
                    .load(exerciseModel.image).placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(binding.ivExerciseImageList)
                isUpdate = true
            }
        }
        binding.btnImage.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {

                    imagePermissionLauncher.launch("image/*")

                }
                else -> {
                    permission.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }
        }
        binding.btnSave.setOnClickListener {
            if(binding.etExerciseName.text.toString().isEmpty()){
                binding.etExerciseName.error = "Enter Exercise name"
            }else if(binding.etExerciseDescription.text.toString().isEmpty()){
                binding.etExerciseName.error = "Enter Description name"
            }else{
                binding.progress.visibility = View.VISIBLE
                var exerciseModel = ExerciseModel()
                exerciseModel.exerciseName = binding.etExerciseName.text.toString()
                exerciseModel.exerciseDescription = binding.etExerciseDescription.text.toString()
                exerciseModel.difficultLevel = difficultyLevel
                exerciseModel.exerciseType = exerciseType
                exerciseModel.exerciseTypeName = exerciseName
                exerciseModel.dayId = dayId
                exerciseModel.WeigthGainOrLoss = weightGORL


                if(this::imageUri.isInitialized) {
                            println("ImageList")
                            var ImagesPath =
                                storageRef.child("/images")
                                    .child(System.currentTimeMillis().toString())
                            ImagesPath.putFile(imageUri)
                                .continueWithTask(object :
                                    Continuation<UploadTask.TaskSnapshot?, Task<Uri?>> {
                                    @Throws(Exception::class)
                                    override fun then(@NonNull task: Task<UploadTask.TaskSnapshot?>): Task<Uri?> {
                                        if (!task.isSuccessful()) {
                                            println("ImageList: TAak")
                                            throw task.getException()!!
                                        }
                                        return ImagesPath.getDownloadUrl()
                                    }
                                }).addOnCompleteListener(object : OnCompleteListener<Uri?> {
                                    override fun onComplete(@NonNull task: Task<Uri?>) {
                                        if (task.isSuccessful()) {
                                            val downUri: Uri? = task.getResult()
                                            Log.e(ContentValues.TAG, "download uri $downUri")
                                            exerciseModel.image = downUri.toString()
                                            if (isUpdate) {
                                                println("IsUpdate: $isUpdate")
                                                db.collection("exercise").document(exerciseId)
                                                    .set(exerciseModel)
                                                    .addOnCompleteListener {
                                                        if (it.isSuccessful) {
                                                            binding.progress.visibility = View.GONE
                                                            Toast.makeText(
                                                                mainActivity,
                                                                "Update Data",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            mainActivity.navController.popBackStack()
                                                        }
                                                    }
                                            } else {
                                                println("IsUpdate: $isUpdate")
                                                db.collection("exercise").add(exerciseModel)
                                                    .addOnCompleteListener {
                                                        if (it.isSuccessful) {
                                                            binding.progress.visibility = View.GONE
                                                            Toast.makeText(
                                                                mainActivity,
                                                                "Data Save",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            mainActivity.navController.popBackStack()
                                                        }
                                                    }
                                            }
                                        } else {
                                            val ERROR: String? = task.getException()?.message
                                            Toast.makeText(
                                                requireContext(),
                                                ERROR,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                })
                        }

                else{
                        Toast.makeText(mainActivity,"Select Image",Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment AddUpdateExerciseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddUpdateExerciseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}