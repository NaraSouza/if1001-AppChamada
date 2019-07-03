package com.example.appchamadainterface

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.*
import kotlinx.android.synthetic.main.activity_reports_student.view.*

class ReportStudentActivity : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_reports_student, container, false)

        view.anyChart.setChart(createPieChart())

        return view
    }

    private fun createPieChart(): com.anychart.charts.Pie {
        val pie = AnyChart.pie()

        val data = ArrayList<DataEntry>()

        data.add(ValueDataEntry("Presença", 88.57))
        data.add(ValueDataEntry("Faltas", 11.43))

        pie.data(data)

        pie.title("IF1001 - Percentual de Presença")
        pie.legend().enabled(true)

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER)

        return pie
    }
}