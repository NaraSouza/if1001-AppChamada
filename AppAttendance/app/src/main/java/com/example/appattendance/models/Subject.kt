package com.example.appattendance.models

class Subject(val name: String, val code: String, val period: String) {

    override fun toString(): String {
        return "$code $name"
    }
}