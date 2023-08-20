package com.example.healthx.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.healthx.R
import com.example.healthx.databinding.MenuFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class MenuFragment: Fragment() {

    private val binding by lazy { MenuFragmentBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list: ArrayList<PieEntry> = ArrayList()
        list.add(PieEntry(8000f))
        list.add(PieEntry(2000f))

        val pieDataSet = PieDataSet(list, "")

        val customColors = mutableListOf<Int>(
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

}