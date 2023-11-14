package com.sekhgmainuddin.learnwithfun.presentation.home

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.ActivityHomeBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import java.io.File


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val menuItems = arrayOf(
            CbnMenuItem(
                R.drawable.ic_home,
                R.drawable.avd_home,
                R.id.homeFragment
            ),
            CbnMenuItem(
                R.drawable.ic_dashboard,
                R.drawable.avd_dashboard,
                R.id.coursesFragment
            ),

            CbnMenuItem(
                R.drawable.ic_profile,
                R.drawable.avd_profile,
                R.id.profileFragment
            )
        )
        binding.bottomNavBar.setMenuItems(menuItems, 0)
        binding.bottomNavBar.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!isChangingConfigurations) {
            deleteTempFiles(cacheDir);
        }
    }

    private fun deleteTempFiles(file: File): Boolean {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.isDirectory) {
                        deleteTempFiles(f)
                    } else {
                        f.delete()
                    }
                }
            }
        }
        return file.delete()
    }

}