package com.example.healthx.ui.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.healthx.R
import com.example.healthx.auth.AuthViewModel
import com.example.healthx.databinding.MenuFragmentBinding
import com.example.healthx.db.DatabaseViewModel
import com.example.healthx.ui.activities.OnboardingActivity
import com.example.healthx.ui.fragments.BaseFragment
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MenuFragment : BaseFragment() {

    private val binding by lazy { MenuFragmentBinding.inflate(layoutInflater) }
    private val viewModel: AuthViewModel by activityViewModels()
    private val databaseViewModel: DatabaseViewModel by activityViewModels()
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setUserKey(OnboardingActivity.key)

        binding.time.text = setGreet()

//        val dataReference = database.getReference("Users")
//
//        viewModel.userKey.observe(viewLifecycleOwner){key->
//            if (key != null) {
//
//                Toast.makeText(requireContext(), key, Toast.LENGTH_SHORT).show()
//                dataReference.child(key).get()
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            if (task.result.exists()) {
//
//                                val dataSnapshot = task.result
//                                val userName = dataSnapshot.child("userName").value
//                                val profileUri = dataSnapshot.child("profilePictureUrl").value
//
//                                binding.userName.text = getFirstTwoWords(userName.toString())
//                                Glide.with(requireContext())
//                                    .load(profileUri)
//                                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
//                                    .into(binding.imageView)
//
//                            }
//                        }
//                    }
//            }
//        }


        databaseViewModel.userDataLiveData.observe(viewLifecycleOwner) {
            val userData = it.last()
            binding.userName.text = getFirstTwoWords(userData.userName.toString())
            Glide.with(requireContext())
                .load(userData.profilePictureUrl)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.imageView)
        }


        setSpinner()


        binding.pedometer.setOnClickListener {
            findNavController().navigate(R.id.action_menu_fragment_to_pedometerFragment)
        }

    }

    private fun setSpinner() {
        // Observe the userDataLiveData
        databaseViewModel.userDataLiveData.observe(viewLifecycleOwner) { userDataList ->
            // Extract the list of dates from your LiveData
            val dateList = userDataList.map { formatDate(it.idAsDate) }.toMutableList()
            dateList.reverse()
            dateList[0] = "Today"
            dateList[1] = "Yesterday"

            // Create an adapter with the extracted dateList
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.custom_spinner_layout,
                dateList
            )

            // Set the adapter for your spinner
            binding.spinner.adapter = adapter

            binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val userData = userDataList[userDataList.size - position - 1]
                    binding.apply {
                        numberOfCurrentSteps.text = userData.steps.toString()
                        calories.text = userData.calories.toInt().toString()
                        numberOfTotalSteps.text = userData.totalSteps.toString()

                    }

                    val list: ArrayList<PieEntry> = ArrayList()
                    list.add(PieEntry(userData.steps.toFloat()))
                    list.add(PieEntry((userData.totalSteps - userData.steps).toFloat()))

                    val pieDataSet = PieDataSet(list, "")

                    val customColors = mutableListOf(
                        ContextCompat.getColor(requireContext(), R.color.themeHeavy),
                        ContextCompat.getColor(requireContext(), R.color.theme)
                    )

                    pieDataSet.colors = customColors
                    pieDataSet.sliceSpace = 1f

                    pieDataSet.valueTextSize = 0f

                    val pieData = PieData(pieDataSet)
                    binding.pieChart.apply {
                        data = pieData
                        description.isEnabled = false
                        transparentCircleRadius = 0f
                        isDrawHoleEnabled = false
                        legend.isEnabled = false
                        isRotationEnabled = false
                        setTouchEnabled(false)
                        animateY(800)
                        animateX(500)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }


        }
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val date = inputFormat.parse(dateString) as Date
        val outputFormat = SimpleDateFormat("d MMM", Locale.US)
        return outputFormat.format(date)
    }

    private fun getFirstTwoWords(sentence: String): String {
        val words = sentence.trim().split("\\s+".toRegex())
        return if (words.size >= 2) {
            "${words[0]} ${words[1]}"
        } else {
            sentence
        }
    }


}