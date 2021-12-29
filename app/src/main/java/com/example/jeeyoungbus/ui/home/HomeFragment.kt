package com.example.jeeyoungbus.ui.home

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jeeyoungbus.databinding.FragmentHomeBinding
import com.example.jeeyoungbus.ui.utils.BusDialog
import kotlinx.android.synthetic.main.dialog_alert_payment.*
import org.json.JSONArray
import org.json.JSONException

const val REQUEST_CODE: Int = 1111

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val dialog = BusDialog()

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
                showPaymentConfirmDialog()
            }
        }
    }

    private fun showPaymentConfirmDialog() {
        dialog.showPaymentAlertDialog(
            context = requireContext(),
            dialogMsg = "Total Sales Amount: ${homeViewModel.sumPrice.value} €\nDo you want to proceed with payment?",
            doOnConfirm = {
                startPaymentOnYavinPay()
            },
            doOnClose = {}
        )
    }

    private fun startPaymentOnYavinPay() {
        val intent = Intent()
        intent.component =
            ComponentName("com.yavin.macewindu", "com.yavin.macewindu.PaymentActivity")
        intent.putExtra("vendorToken", "<vendor_token>")
        intent.putExtra(
            "amount",
            homeViewModel.sumPrice.value!!.toFloat().times(100).toInt().toString()
        )
        intent.putExtra("currency", "EUR")
        intent.putExtra("transactionType", "Debit")
        intent.putExtra("reference", "12345")
        intent.putExtra("client", "{\"phone\":\"0611223344\",\"email\":\"client@client.com\"}")
        val jArray =
            JSONArray("[\"hello printer\", \"this is a\", \"    wonderful\", \"        TICKET\"]")
        val receiptTicket = ArrayList<String>()
        for (i in 0 until jArray.length()) {
            try {
                receiptTicket.add(jArray.getString(i))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        intent.putExtra("receiptTicket", receiptTicket)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_CODE) {
            val bundle: Bundle = intent!!.extras!!
            showPaymentResultDialog(bundle)
        }
    }

    private fun showPaymentResultDialog(bundle: Bundle) {

        dialog.showPaymentAlertDialog(
            context = requireContext(),
            dialogMsg = "ClientTicket: ${bundle["clientTicket"]}",
            doOnConfirm = {
                homeViewModel.apply {
                    singleJourneyQty.postValue(defaultQty)
                    dayQty.postValue(defaultQty)
                    weekQty.postValue(defaultQty)
                }
            },
            doOnClose = {},
            isResult = true
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}