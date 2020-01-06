package com.keelim.cnubus.temp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivityTempBinding

import kotlinx.android.synthetic.main.activity_temp.*

class TempActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_temp)

        var binding: ActivityTempBinding = DataBindingUtil.setContentView(this, R.layout.activity_temp);
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        binding.tvSample.text = "임시 화면 입니다."
        subscribeUi(binding)
    }

    private fun subscribeUi(binding: ActivityTempBinding) {
        var factory = TempViewModelFactory()
        var viewModel:TempViewModel = ViewModelProviders.of(this, factory).get(TempViewModel::class.java)

        viewModel.clickConverter.observe(this, Observer { Toast.makeText(this, "${binding.user?.name}, ${binding.user?.address}", Toast.LENGTH_SHORT).show() })
        binding.user = User("hello", "hello", R.drawable.common_full_open_on_phone)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

    }

}



