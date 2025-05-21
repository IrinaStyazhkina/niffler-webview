package ru.niffer_android.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.niffer_android.R
import ru.niffer_android.databinding.ActivityMainBinding
import ru.niffer_android.ui.auth.SignInViewModel
import ru.niffer_android.ui.newSpending.EditSpendingViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarLayout.toolbar)

        binding.toolbarLayout.addSpendingButton.setOnClickListener {
            findNavController(R.id.nav_host_fragment)
                .navigate(R.id.action_global_addNewSpendingFragment)
        }

        binding.toolbarLayout.tvNifflerTitle.setOnClickListener {
            findNavController(R.id.nav_host_fragment)
                .navigate(R.id.action_global_toMainPage)
        }

        binding.toolbarLayout.ivNifflerIcon.setOnClickListener {
            findNavController(R.id.nav_host_fragment)
                .navigate(R.id.action_global_toMainPage)
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.main_page -> {
                    findNavController(R.id.nav_host_fragment)
                        .navigate(R.id.action_global_toMainPage)
                    true
                }
                R.id.profile_item -> {
                    findNavController(R.id.nav_host_fragment)
                        .navigate(R.id.action_global_toAccountFragment)
                    true
                }
                R.id.friends_item -> {
                    findNavController(R.id.nav_host_fragment)
                        .navigate(R.id.action_global_toFriendsFragment)
                    true
                }
                R.id.all_people_item -> {
                    findNavController(R.id.nav_host_fragment)
                        .navigate(R.id.action_global_toAllPeople)
                    true
                }
                else -> false
            }
        }
    }

    fun showSuccess(message: String) {
        Snackbar
            .make(binding.root, message, Snackbar.LENGTH_LONG).apply {
                setBackgroundTint(
                    ContextCompat.getColor(this@MainActivity,
                        R.color.green
                    )
                )
                setTextColor(
                    ContextCompat.getColor(this@MainActivity,
                        R.color.gray_50
                    )
                )
            }
            .show()
    }

    fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(
                ContextCompat.getColor(this@MainActivity,
                    R.color.red
                )
            )
            setTextColor(
                ContextCompat.getColor(this@MainActivity,
                    R.color.gray_50
                )
            )
        }.show()
    }

    fun showLoader() {
        binding.loader.isVisible = true
    }

    fun hideLoader() {
        binding.loader.isVisible = false
    }
}