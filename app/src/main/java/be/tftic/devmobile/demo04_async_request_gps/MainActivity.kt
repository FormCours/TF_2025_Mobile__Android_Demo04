package be.tftic.devmobile.demo04_async_request_gps

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.tftic.devmobile.demo04_async_request_gps.activities.ExampleAsyncActivity
import be.tftic.devmobile.demo04_async_request_gps.activities.ExampleGpsActivity
import be.tftic.devmobile.demo04_async_request_gps.activities.ExampleRequestActivity
import be.tftic.devmobile.demo04_async_request_gps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            // Fix color pour Android < 12
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.pinky)
                window.navigationBarColor = ContextCompat.getColor(this, R.color.pinky)
            }

            insets
        }

        binding.btnMainAsync.setOnClickListener {
            openActivity(ExampleAsyncActivity::class.java)
        }
        binding.btnMainRequest.setOnClickListener {
            openActivity(ExampleRequestActivity::class.java)
        }
        binding.btnMainGps.setOnClickListener {
            openActivity(ExampleGpsActivity::class.java)
        }
    }

    private fun openActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }
}