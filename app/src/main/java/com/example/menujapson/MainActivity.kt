package com.example.menujapson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.menujapson.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)  // Set the toolbar as the ActionBar
        supportActionBar?.title = "Pon Japson"
        supportActionBar?.subtitle = "Menu Activity"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        // Enable icons in the overflow menu
        if (menu.javaClass.simpleName.equals("MenuBuilder", ignoreCase = true)) {
            try {
                val method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
                method.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_navigate -> {
                // Navigate to another fragment
                replaceFragment(SampleFragment())
                true
            }
            R.id.menu_dialog -> {
                // Show a DialogFragment
                val dialog = CustomDialog()
                dialog.show(supportFragmentManager, "CustomDialogFragment")
                true
            }
            R.id.menu_exit -> {
                // Exit the app
                finishAffinity()  // Finish the app
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
