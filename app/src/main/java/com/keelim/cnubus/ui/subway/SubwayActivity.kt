package com.keelim.cnubus.ui.subway

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.ActivitySubwayBinding
import com.keelim.cnubus.ui.subway.stationarrivals.StationArrivalsFragmentArgs
import com.keelim.cnubus.utils.toGone
import com.keelim.cnubus.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubwayActivity:AppCompatActivity() {
    private val binding: ActivitySubwayBinding by lazy { ActivitySubwayBinding.inflate(layoutInflater) }
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViews() = with(binding){
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navigationController)
        navigationController.addOnDestinationChangedListener { _, destination, argument ->
            if (destination.id == R.id.station_arrivals_dest) {
                title = StationArrivalsFragmentArgs.fromBundle(argument!!).station.name
                toolbar.toVisible()
            } else {
                toolbar.toGone()
            }
        }
    }
}