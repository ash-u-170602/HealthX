package com.example.healthx.ui.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MenuFragment : BaseFragment() {

    private val binding by lazy { MenuFragmentBinding.inflate(layoutInflater) }
    private val viewModel: AuthViewModel by activityViewModels()
    private val databaseViewModel: DatabaseViewModel by activityViewModels()
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private var dataList: List<>


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


        val list: ArrayList<PieEntry> = ArrayList()
        list.add(PieEntry(8000f))
        list.add(PieEntry(2000f))

        val pieDataSet = PieDataSet(list, "")

        val customColors = mutableListOf(
            ContextCompat.getColor(requireContext(), R.color.theme),
            ContextCompat.getColor(requireContext(), R.color.themeHeavy)
        )

        pieDataSet.colors = customColors
        pieDataSet.sliceSpace = 4f

        pieDataSet.valueTextSize = 0f

        val pieData = PieData(pieDataSet)
        binding.pieChart.apply {
            data = pieData
            description.text = ""
            transparentCircleRadius = 0f
            isDrawHoleEnabled = false
            legend.isEnabled = false
            isRotationEnabled = false
            setTouchEnabled(false)
            animateY(800)
            animateX(500)
        }

        binding.pedometer.setOnClickListener {
            findNavController().navigate(R.id.action_menu_fragment_to_pedometerFragment)
        }

    }

    private fun setSpinner() {

        val customList = listOf(
            "Today",
            "Yesterday",
            formatDate(getDayBeforeYesterday(2)),
            formatDate(getDayBeforeYesterday(3)),
            formatDate(getDayBeforeYesterday(4))
        )

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_layout,
            customList
        )

        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun getDayBeforeYesterday(day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -day)
        return calendar.time
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("dd MMM", Locale.getDefault())
        return format.format(date)
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