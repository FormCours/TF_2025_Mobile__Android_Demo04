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
import be.tftic.devmobile.demo04_async_request_gps.databinding.ActivityExampleAsyncBinding
import be.tftic.devmobile.demo04_async_request_gps.databinding.ActivityExampleRequestBinding
import be.tftic.devmobile.demo04_async_request_gps.db.DbContract
import be.tftic.devmobile.demo04_async_request_gps.db.dao.ProductDao
import be.tftic.devmobile.demo04_async_request_gps.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ExampleAsyncActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExampleAsyncBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityExampleAsyncBinding.inflate(layoutInflater)
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

        // Demo ↓
        lifecycleScope.launch {
            // Type de Dispatchers :
            // - Main : UI
            // - IO : Entrée/sortie (Database, Fichier, réseau)
            // - Default : CPU (Pour les traitements "intensif")

            // On créer un traitement sur un thead CPU
            withContext(Dispatchers.Default) {

                // On attent 2 secondes
                delay(2_000)

                // Afficher un toast
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ExampleAsyncActivity, "Bientot l'acces au donnée !", Toast.LENGTH_LONG).show()
                }

                // On attent 2 secondes
                delay(2_000)
            }

            val productDao = ProductDao(this@ExampleAsyncActivity)
            val product = Product(
                0,
                "Exemple d'ajout async",
                (1000000000000..9999999999999).random().toString(),
                3.14,
                null,
                LocalDate.now(),
                true
            )
            productDao.openWritable()
            productDao.insert(product) // ← Thread IO
            productDao.close()
        }
    }
}