package com.example.appchamadainterface.models

class Subject(val title: String, val classes: ArrayList<String>, val maxMissedClasses: Int) {

    override fun toString(): String {
        return title
    }
}