package com.example.appattendance.models

class Subject(val name: String, val code: String) {
    var period = ""

    override fun toString(): String {
        return "$code $name"
    }
}