package com.shamalrathnayake.moviemate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shamalrathnayake.moviemate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rootLayout = findViewById<View>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())


            view.setPadding(
                view.paddingLeft,
                systemBarsInsets.top,
                view.paddingRight,
                view.paddingBottom
            )

            WindowInsetsCompat.CONSUMED
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MoviesFragment())
                .commit()
        }
    }
}