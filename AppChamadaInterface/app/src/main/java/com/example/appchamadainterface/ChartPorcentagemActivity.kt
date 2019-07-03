package com.example.appchamadainterface

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import kotlinx.android.synthetic.main.activity_chart.*
import com.anychart.AnyChart.pie
import com.anychart.enums.*


class ChartPorcentagemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        anyChart.setChart(createPieChart())

    }

    private fun createPieChart(): com.anychart.charts.Pie {
        val pie = AnyChart.pie()

        val data = ArrayList<DataEntry>()

        data.add(ValueDataEntry("Presentes", 88.57))
        data.add(ValueDataEntry("Ausentes", 11.43))

        pie.data(data)

        pie.title("IF1001 (2018.2) - Percentual de Presença")
        pie.legend().enabled(true)

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER)

        return pie
    }
}