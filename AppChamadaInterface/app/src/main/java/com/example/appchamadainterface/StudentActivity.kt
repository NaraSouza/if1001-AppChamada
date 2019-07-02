package com.example.appchamadainterface

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_student.drawer_layout
import kotlinx.android.synthetic.main.activity_student.nav_view
import kotlinx.android.synthetic.main.activity_student.toolbarTop

class StudentActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        nav_view.setNavigationItemSelectedListener(this)

        var toggle = ActionBarDrawerToggle(this, drawer_layout, toolbarTop, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SubjectStudentActivity()).commit()
            nav_view.setCheckedItem(R.id.subject)
        }

    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.subject -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SubjectActivity()).commit()
            }
//            R.id.profile -> {
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
//            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}