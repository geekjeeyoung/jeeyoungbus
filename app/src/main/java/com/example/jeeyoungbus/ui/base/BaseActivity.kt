package com.example.jeeyoungbus.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KClass

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private lateinit var binding: T

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    fun <T : AppCompatActivity> startActivity(cls: KClass<T>) {
        val intent = Intent(this, cls.java)
        startActivity(intent)
    }

    fun <T : AppCompatActivity> startActivity(
        cls: KClass<T>,
        callbackOnStartActivity: (intent: Intent) -> Unit
    ) {
        val intent = Intent(this, cls.java)
        callbackOnStartActivity(intent)
        startActivity(intent)
    }

    fun startActivityResult(
        callbackOnStartActivityResult: (result: ActivityResult) -> Unit
    ): ActivityResultLauncher<Intent> = registerForActivityResult(
        StartActivityForResult()
    ) { callbackOnStartActivityResult(it) }

    fun requestPermission(
        callbackOnRequestPermitted: () -> Unit,
        deniedSomething: () -> Unit
    ): ActivityResultLauncher<String> {
        return registerForActivityResult(
            RequestPermission()
        ) { isPermissionGranted ->
            if (isPermissionGranted) {
                callbackOnRequestPermitted()
            } else {
                deniedSomething()
            }
        }
    }

    fun getContent(callbackOnGetContent: (uri: Uri) -> Unit): ActivityResultLauncher<String> {
        return registerForActivityResult(GetContent()) {
            it?.let { callbackOnGetContent(it) }
        }
    }
}