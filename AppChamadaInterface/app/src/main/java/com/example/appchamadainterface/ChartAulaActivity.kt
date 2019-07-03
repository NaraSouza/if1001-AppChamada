package com.example.appchamadainterface

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import kotlinx.android.synthetic.main.activity_chart.*

class ChartAulaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        anyChart.setChart(createBarChart())

    }

    private fun createBarChart(): com.anychart.charts.Cartesian? {
        val cartesian = AnyChart.column()

        val presentes = ArrayList<DataEntry>()
        val ausentes = ArrayList<DataEntry>()

        presentes.add(ValueDataEntry("Aula 1", 30))
        presentes.add(ValueDataEntry("Aula 2", 25))
        presentes.add(ValueDataEntry("Aula 3", 27))
        presentes.add(ValueDataEntry("Aula 4", 12))
        presentes.add(ValueDataEntry("Aula 5", 22))

        ausentes.add(ValueDataEntry("Aula 1", 0))
        ausentes.add(ValueDataEntry("Aula 2", 5))
        ausentes.add(ValueDataEntry("Aula 3", 3))
        ausentes.add(ValueDataEntry("Aula 4", 18))
        ausentes.add(ValueDataEntry("Aula 5", 8))

        val presentesColumn = cartesian.column(presentes)
        presentesColumn.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER)
                .anchor(Anchor.CENTER)
                .offsetX(0.0)
                .offsetY(5.0)
                .format("{%Value}")
        presentesColumn.name("Presentes")

        val ausentesColumn = cartesian.column(ausentes)
        ausentesColumn.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER)
                .anchor(Anchor.CENTER)
                .offsetX(0.0)
                .offsetY(5.0)
                .format("{%Value}")
        ausentesColumn.name("Ausentes")

        cartesian.title("IF1001 (2019.1) - Presentes x Ausentes")

        cartesian.yScale().minimum(0.0)
        cartesian.yAxis(0).labels().format("{%Value}")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.animation(true)
        cartesian.legend().enabled(true)

        cartesian.xAxis(0).title("Aulas")
        cartesian.yAxis(0).title("Quantidade")

        return cartesian
    }
}