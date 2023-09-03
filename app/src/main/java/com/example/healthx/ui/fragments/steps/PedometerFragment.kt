package com.example.healthx.ui.fragments.steps

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.healthx.R
import com.example.healthx.databinding.PedometerFragmentBinding
import com.example.healthx.db.DatabaseViewModel
import com.example.healthx.services.StepCounterService
import com.example.healthx.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.healthx.util.Constants.ACTION_STOP_SERVICE
import com.example.healthx.util.todayDate

class PedometerFragment : Fragment() {

    private val binding by lazy { PedometerFragmentBinding.inflate(layoutInflater) }
    private val databaseViewModel: DatabaseViewModel by activityViewModels()
    private var isWalking = false
    private var currSteps: Int = 0
    private var currCalories: Float = 0f
    private var currDistance: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.circularProgressBar.apply {
            progressMax = 50f
        }

        databaseViewModel.userDataLiveData.observe(viewLifecycleOwner) {
            val data = it.last()
            currSteps = data.steps
            currCalories = data.calories
            currDistance = data.distance
            StepCounterService.totalSteps = currSteps.toFloat()
            binding.apply {
                steps.text = currSteps.toString()
                calories.text = currCalories.toString()
                distance.text = currDistance.toString()
                circularProgressBar.progress = currSteps.toFloat()
            }
        }

        updateUIFromServiceState()

        binding.startOrPause.setOnClickListener {
            if (!isWalking) {
                isWalking = true
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            } else {
                isWalking = false
                Toast.makeText(requireContext(), currSteps.toString(), Toast.LENGTH_SHORT).show()
                databaseViewModel.updatePedometerDetails(
                    todayDate(),
                    currSteps,
                    currCalories,
                    currDistance
                )
                sendCommandToService(ACTION_STOP_SERVICE)
            }

            updateUIFromServiceState()
        }


        StepCounterService.stepCountLiveData.observe(viewLifecycleOwner) {
            binding.steps.text = it.toInt().toString()
            binding.circularProgressBar.setProgressWithAnimation(it)
            binding.calories.text = String.format("%.2f", getCalculatedCalories(it))
            binding.distance.text = String.format("%.2f", getCalculatedDistance(it))
            currSteps = it.toInt()
            currCalories = getCalculatedCalories(it).toFloat()
            currDistance = getCalculatedDistance(it).toFloat()
        }


        StepCounterService.elapsedTimeLiveData.observe(viewLifecycleOwner) {
            binding.time.text = it.toString()
            isWalking = true
            updateUIFromServiceState()
        }

    }

    private fun sendCommandToService(action: String) {
        Intent(requireContext(), StepCounterService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun updateUIFromServiceState() {
        if (isWalking) {
            binding.startOrPause.setImageResource(R.drawable.baseline_pause_circle_filled_24)
        } else {
            binding.startOrPause.setImageResource(R.drawable.baseline_play_circle_filled_24)
        }
    }

    private fun getCalculatedCalories(steps: Float) = steps * 0.03

    private fun getCalculatedDistance(steps: Float) = steps * 0.76

    override fun onPause() {
        super.onPause()
        databaseViewModel.updatePedometerDetails(
            todayDate(),
            currSteps,
            currCalories,
            currDistance
        )
    }

}