package com.dotjoo.aghslilnidelivery.ui.activity

 import android.os.Bundle
import androidx.core.view.isVisible
 import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseActivity
import com.dotjoo.aghslilnidelivery.databinding.ActivityMainBinding
 import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.progress = baseShowProgress
        setupNavController()
    }

    fun showBottomNav(isVisible: Boolean) {
        binding.navView.isVisible = isVisible
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        var navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null

    }

}