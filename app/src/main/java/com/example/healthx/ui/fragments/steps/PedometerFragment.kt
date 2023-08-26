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
import com.example.healthx.databinding.PedometerFragmentBinding
import com.example.healthx.services.StepCounterService
import com.example.healthx.util.Constants.ACTION_START_OR_RESUME_SERVICE

class PedometerFragment : Fragment() {

    private val binding by lazy { PedometerFragmentBinding.inflate(layoutInflater) }

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
            setProgressWithAnimation(2000f, 1000)
            progressMax = 6000f
        }

        binding.startOrPause.setOnClickListener {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }

        StepCounterService.stepCountLiveData.observe(viewLifecycleOwner){
            binding.steps.text = it.toString()
            Toast.makeText(requireContext(), "Chal rha h bhai", Toast.LENGTH_SHORT).show()
        }

    }

    private fun sendCommandToService(action: String) {
        Intent(requireContext(), StepCounterService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }


}