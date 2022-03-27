package ru.qa.interceptor

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.qa.interceptor.databinding.ActivityInterceptorBinding
import ru.qa.interceptor.viewmodel.RequestViewModel

class InterceptorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterceptorBinding
    private val model: RequestViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterceptorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBar)

        navController = findNavController(R.id.nav_host)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.interceptor_menu, menu)

        menu.findItem(R.id.clearAllAction)?.setOnMenuItemClickListener {
            if (navController.currentDestination?.id  == R.id.navDetailRequest){
                navController.popBackStack()
            }

            model.deleteAll()
            true
        }

        return super.onCreateOptionsMenu(menu)
    }
}