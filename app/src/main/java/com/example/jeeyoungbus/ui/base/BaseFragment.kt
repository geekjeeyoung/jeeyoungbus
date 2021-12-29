package com.example.jeeyoungbus.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    lateinit var binding: T

    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    fun <T : AppCompatActivity> startActivity(cls: KClass<T>) {
        val intent = Intent(requireActivity(), cls.java)
        startActivity(intent)
    }

    fun <T : AppCompatActivity> startActivity(
        cls: KClass<T>,
        callbackOnStartActivity: (intent: Intent) -> Unit
    ) {
        val intent = Intent(requireActivity(), cls.java)
        callbackOnStartActivity(intent)
        startActivity(intent)
    }

    fun startActivityResult(
        callbackOnStartActivityResult: (result: ActivityResult) -> Unit
    ): ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { callbackOnStartActivityResult(it) }

    fun requestPermission(
        callbackOnRequestPermitted: () -> Unit,
        deniedSomething: () -> Unit
    ): ActivityResultLauncher<String> {
        return registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isPermissionGranted ->
            if (isPermissionGranted) {
                callbackOnRequestPermitted()
            } else {
                deniedSomething()
            }
        }
    }

    fun getContent(callbackOnGetContent: (uri: Uri) -> Unit): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { callbackOnGetContent(it) }
        }
    }

}