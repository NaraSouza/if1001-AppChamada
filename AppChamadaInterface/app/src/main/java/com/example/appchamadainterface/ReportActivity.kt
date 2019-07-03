package com.example.appchamadainterface

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.Position
import com.anychart.enums.HoverMode
import com.anychart.enums.TooltipPositionMode
import kotlinx.android.synthetic.main.activity_reports_professor.view.*
import com.anychart.enums.MarkerType


class ReportActivity : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_reports_professor, container, false)

        view.buttonAula.setOnClickListener {
            startActivity(Intent(context, ChartAulaActivity::class.java))
        }

        view.buttonPeriodo.setOnClickListener {
            startActivity(Intent(context, ChartPeriodoActivity::class.java))
        }

        view.buttonPorcentagem.setOnClickListener {
            startActivity(Intent(context, ChartPorcentagemActivity::class.java))
        }

        return view
    }
}