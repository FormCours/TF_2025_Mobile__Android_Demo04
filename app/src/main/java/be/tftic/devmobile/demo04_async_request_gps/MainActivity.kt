package be.tftic.devmobile.demo04_async_request_gps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            else {
                openActivity(ExampleGpsActivity::class.java)
            }
        }
    }

    private fun openActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }

    val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        // Traitement de la permission
        // https://developer.android.com/training/permissions/requesting?hl=fr

        if(!isGranted) {
            Toast.makeText(this, "La location est necessaire pour cette écran !", Toast.LENGTH_LONG).show()
        }
        else{
            openActivity(ExampleGpsActivity::class.java)
        }
    }
}