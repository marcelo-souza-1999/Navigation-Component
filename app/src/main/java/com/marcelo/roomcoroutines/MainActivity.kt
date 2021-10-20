package com.marcelo.roomcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marcelo.roomcoroutines.ui.profile.ProfileFragment
import com.marcelo.roomcoroutines.ui.start.StartFragment

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupFragments()
    }

    private fun setupFragments() {

    }
}