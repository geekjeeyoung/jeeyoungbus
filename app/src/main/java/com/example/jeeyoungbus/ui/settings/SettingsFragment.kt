package com.example.jeeyoungbus.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jeeyoungbus.BusApp
import com.example.jeeyoungbus.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settingsViewModel.apply {
            singleJourneyTicketPrice.observe(viewLifecycleOwner, {
                binding.etSingleJourneyTicketPrice.setText(it)
            })
            dayTicketPrice.observe(viewLifecycleOwner, {
                binding.etDayTicketPrice.setText(it)
            })
            weekTicketPrice.observe(viewLifecycleOwner, {
                binding.etWeekTicketPrice.setText(it)
            })
            showLoadingPb.observe(viewLifecycleOwner, {
                if (it) {
                    binding.pbLoading.visibility = View.VISIBLE
                } else {
                    binding.pbLoading.visibility = View.GONE
                }
            })
            showToast.observe(viewLifecycleOwner, {
                if (it.first) {
                    Toast.makeText(activity, it.second, LENGTH_SHORT).show()
                }
            })
        }

        binding.btnChange.setOnClickListener {
            settingsViewModel.onChangeClicked(
                binding.etSingleJourneyTicketPrice.text.toString(),
                binding.etDayTicketPrice.text.toString(),
                binding.etWeekTicketPrice.text.toString()
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}