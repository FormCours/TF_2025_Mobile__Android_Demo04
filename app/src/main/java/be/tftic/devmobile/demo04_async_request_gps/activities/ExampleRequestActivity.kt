package be.tftic.devmobile.demo04_async_request_gps.activities

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import be.tftic.devmobile.demo04_async_request_gps.R
import be.tftic.devmobile.demo04_async_request_gps.databinding.ActivityExampleRequestBinding
import be.tftic.devmobile.demo04_async_request_gps.services.getPersonInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExampleRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityExampleRequestBinding.inflate(layoutInflater)
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

        binding.btnExampleRequestGetUser.setOnClickListener {
            loadUser();
        }
    }

    private fun loadUser() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                binding.tvExampleRequestResult.text = "🧟‍♀️ Chargement 🧟‍♂️"
            }

            val person = getPersonInfo();

            withContext(Dispatchers.Main) {
                binding.tvExampleRequestResult.text = "Bonjour ${person.firstname} ${person.lastname}"
            }
        }
    }
}