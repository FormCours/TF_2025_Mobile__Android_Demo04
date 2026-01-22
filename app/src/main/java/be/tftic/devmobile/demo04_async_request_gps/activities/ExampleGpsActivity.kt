package be.tftic.devmobile.demo04_async_request_gps.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.tftic.devmobile.demo04_async_request_gps.R
import be.tftic.devmobile.demo04_async_request_gps.databinding.ActivityExampleGpsBinding
import be.tftic.devmobile.demo04_async_request_gps.databinding.ActivityExampleRequestBinding

class ExampleGpsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExampleGpsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityExampleGpsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}