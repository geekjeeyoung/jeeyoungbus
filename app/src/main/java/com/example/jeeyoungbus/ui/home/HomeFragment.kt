package com.example.jeeyoungbus.ui.home

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jeeyoungbus.databinding.FragmentHomeBinding
import android.widget.Toast

import android.text.Spanned


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.lifecycleOwner = this

        setupViewModel()
        setupView()

        return root
    }

    private fun setupViewModel() {
        binding.vm = homeViewModel
        homeViewModel.apply {
            singleJourneyTicketPrice.observe(viewLifecycleOwner, {
                binding.tvSingleJourneyTicketPrice.text = it
            })
            dayTicketPrice.observe(viewLifecycleOwner, {
                binding.tvDayTickeyPrice.text = it
            })
            weekTicketPrice.observe(viewLifecycleOwner, {
                binding.tvWeekTicketPrice.text = it
            })
            sumPrice.observe(viewLifecycleOwner, {
                binding.tvSumPrice.text = it
            })
            showLoadingPb.observe(viewLifecycleOwner, {
                if (it) {
                    binding.pbLoading.visibility = View.VISIBLE
                } else {
                    binding.pbLoading.visibility = View.GONE
                }
            })
        }
    }

    private fun setupView() {
        val filterNonDigit = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        }
        val filterLength: InputFilter = LengthFilter(6)
        val filters = arrayOf(filterNonDigit, filterLength)
        binding.apply {
            etSingleJourneyQty.filters = filters
            etDayTicketQty.filters = filters
            etWeekTicketQty.filters = filters
            btnSell.setOnClickListener {
                homeViewModel.onSellClicked()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}