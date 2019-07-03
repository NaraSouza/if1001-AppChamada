package com.example.appchamadainterface

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import kotlinx.android.synthetic.main.activity_chart.*

class ChartPeriodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        anyChart.setChart(createLineChart())

    }

    private fun createLineChart(): com.anychart.charts.Cartesian? {
        val cartesian2 = AnyChart.line()
        cartesian2.animation(true)

        cartesian2.padding(10.0, 20.0, 5.0, 20.0)

        cartesian2.crosshair().enabled(true)
        cartesian2.crosshair()
                .yLabel(true)

        cartesian2.tooltip().positionMode(TooltipPositionMode.POINT)

        cartesian2.title("IF1001 - Presentes x Ausentes")

        cartesian2.xAxis(0).title("Períodos")
        cartesian2.yAxis(0).title("Quantidade")
        cartesian2.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val presentesData = ArrayList<DataEntry>()
        presentesData.add(ValueDataEntry("2017.1", 512))
        presentesData.add(ValueDataEntry("2017.2", 527))
        presentesData.add(ValueDataEntry("2018.1", 419))
        presentesData.add(ValueDataEntry("2018.2", 620))
        presentesData.add(ValueDataEntry("2019.1", 680))

        val series1 = cartesian2.line(presentesData)
        series1.name("Presentes")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4.0)
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5.0)
                .offsetY(5.0)


        val ausentesData = ArrayList<DataEntry>()
        ausentesData.add(ValueDataEntry("2017.1", 188))
        ausentesData.add(ValueDataEntry("2017.2", 173))
        ausentesData.add(ValueDataEntry("2018.1", 281))
        ausentesData.add(ValueDataEntry("2018.2", 80))
        ausentesData.add(ValueDataEntry("2019.1", 20))

        val series2 = cartesian2.line(ausentesData)
        series2.name("Ausentes")
        series2.hovered().markers().enabled(true)
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4.0)
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5.0)
                .offsetY(5.0)

        cartesian2.legend().enabled(true)
        cartesian2.legend().fontSize(13.0)
        cartesian2.legend().padding(0.0, 0.0, 10.0, 0.0)

        return cartesian2
    }
}