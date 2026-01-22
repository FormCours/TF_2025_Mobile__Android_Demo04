package be.tftic.devmobile.demo04_async_request_gps.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.tftic.devmobile.demo04_async_request_gps.R
import be.tftic.devmobile.demo04_async_request_gps.databinding.ActivityExampleGpsBinding
import be.tftic.devmobile.demo04_async_request_gps.databinding.ActivityExampleRequestBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class ExampleGpsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExampleGpsBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityExampleGpsBinding.inflate(layoutInflater)
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

        // Récuperation de l'utilitaire pour obtenir la localitation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Listener sur le bouton
        binding.btnExampleGpsCurrent.setOnClickListener {
            getPositionGPS()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getPositionGPS() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                binding.tvExampleGpsResult.text = "Coordonnée : ${location.latitude} ${location.longitude}"
            }
            .addOnFailureListener { exception ->
                binding.tvExampleGpsResult.text = "Erreur lors de la récuperation des Coordonnées"
            }
    }


}