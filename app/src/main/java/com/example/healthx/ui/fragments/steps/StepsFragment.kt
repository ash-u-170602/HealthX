package com.example.healthx.ui.fragments.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.healthx.R
import com.example.healthx.databinding.StepsFragmentBinding
import com.example.healthx.db.DatabaseViewModel
import com.example.healthx.ui.fragments.BaseFragment
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StepsFragment : BaseFragment() {

    private val binding by lazy { StepsFragmentBinding.inflate(layoutInflater) }
    private val databaseViewModel: DatabaseViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.time.text = setGreet()

        databaseViewModel.userDataLiveData.observe(viewLifecycleOwner) { userData ->

            binding.userName.text = getFirstTwoWords(userData.userName.toString())
            Glide.with(requireContext())
                .load(userData.profilePictureUrl)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.imageView)

        }

        setSpinner()
        serBarGraph()


        binding.pedometer.setOnClickListener {
            findNavController().navigate(R.id.action_steps_fragment_to_pedometerFragment)
        }

    }

    private fun serBarGraph() {
        val list: ArrayList<BarEntry> = ArrayList()
        list.add(BarEntry(1f, 1040f))
        list.add(BarEntry(2f, 2023f))
        list.add(BarEntry(3f, 3000f))
        list.add(BarEntry(4f, 4000f))
        list.add(BarEntry(5f, 5000f))
        list.add(BarEntry(6f, 6500f))
        list.add(BarEntry(7f, 7090f))

        val barDataSet = BarDataSet(list, "")
        val barChartRender = CustomBarChartRender(
            binding.barGraph,
            binding.barGraph.animator,
            binding.barGraph.viewPortHandler
        )
        barChartRender.setRadius(30)

        barDataSet.valueTextSize = 0f

        val customColors = mutableListOf(
            ContextCompat.getColor(requireContext(), R.color.green),
            ContextCompat.getColor(requireContext(), R.color.greenLight)
        )

        barDataSet.colors = customColors

        barDataSet.valueTextSize = 0f

        val barData = BarData(barDataSet)

        binding.barGraph.apply {
            data = barData
            legend.isEnabled = false
            description.isEnabled = false
            animateY(500)

            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)

            axisRight.isEnabled = false
            xAxis.isEnabled = false

            barData.barWidth = 0.8f
            setTouchEnabled(false)

            renderer = barChartRender

            invalidate()
        }
    }

    private fun setSpinner() {

        val customList = listOf("This Week", "Last Week")

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_layout,
            customList
        )

        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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