package com.fahamin.loadandcachingimage

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.fahamin.loadandcachingimage.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var job: Job? = null
    lateinit var result: Deferred<Bitmap>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

// if we have a previous cached image it will display
        initImageView();

        binding.button.setOnClickListener(View.OnClickListener {
//to make sure only one connection at the same time
            if (job == null || job?.isActive == false)
                fetchNewImage()
        })
    }

    private fun fetchNewImage() {
        //disable the button
        binding.button.isEnabled = false

        result = CoroutineScope(Dispatchers.IO).async {
            try {
                // get an image from URL
                CachingUtils.getBitmapFromUrl()
            }catch (e :IOException) {
                // handle the error
                withContext(Dispatchers.Main){

                    Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                    binding.button.isEnabled = true

                    // cancle the couroutine job worker
                    job?.cancel()
                    null
                }
            }!!
        }
        job = CoroutineScope(Dispatchers.IO).launch {
            // it will waiting to get result
            result.await()?.apply {
                //save bitmap as cached image
                CachingUtils.storeBitmap(this,this@MainActivity)


                // display bitma and enable the button
                withContext(Dispatchers.Main)
                {
                    binding.imageView.setImageBitmap(this@apply)
                    binding.button.isEnabled = true
                }
            }
        }


    }

    private fun initImageView() {
        CachingUtils.loadBitmap(this).apply {
            // if we have one it will display it
            binding.imageView.setImageBitmap(this)

        }
    }
}