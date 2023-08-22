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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.healthx.R
import com.example.healthx.auth.AuthViewModel
import com.example.healthx.databinding.MenuFragmentBinding
import com.example.healthx.ui.fragments.BaseFragment
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MenuFragment : BaseFragment() {

    private val binding by lazy { MenuFragmentBinding.inflate(layoutInflater) }
    private lateinit var viewModel: AuthViewModel
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        database = FirebaseDatabase.getInstance()

        val dataReference = database.getReference("Users")
        dataReference.child("0m2KVjtsvWXv49The6VnwirGlBw2 Mohd Ashad Naushad").get().addOnCompleteListener{task->
            if (task.isSuccessful){
                if (task.result.exists()){

                    val dataSnapshot = task.result
                    val userName = dataSnapshot.child("userName").value
                    val profileUri = dataSnapshot.child("profilePictureUrl").value

                    binding.userName.text = userName.toString()
                    Glide.with(requireContext()).load(profileUri).into(binding.imageView)

                }


            }


        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.time.text = setGreet()

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
                Toast.makeText(
                    requireContext(),
                    "Selected ${adapterView?.getItemAtPosition(position).toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


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


    }

    private fun getDayBeforeYesterday(day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -day) // Subtract 2 days to get the day before yesterday
        return calendar.time
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("dd MMM", Locale.getDefault())
        return format.format(date)
    }


}