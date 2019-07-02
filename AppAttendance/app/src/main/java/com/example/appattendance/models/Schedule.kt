package com.example.appattendance.models

class Schedule(val timerange: String, val weekday: String) {

    override fun toString(): String {
        return "$weekday $timerange"
    }
}