@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.generated.ChartValue
import com.github.rafaelldi.diagnosticsclientplugin.generated.ChartValueType
import com.intellij.ui.JBColor
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.charts.*
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Graphics2D
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CounterChartPanel : BorderLayoutPanel() {
    companion object {
        private const val timeRange = 60
        private val labelFormatter = DateTimeFormatter
            .ofPattern("HH:mm:ss")
            .withZone(ZoneId.systemDefault())
    }

    private val cpuChart = lineChart<Long, Double> {
        ranges {
            yMin = 0.0
            yMax = 100.0
        }
        grid {
            yLines = generator(25.0)
            xLines = generator(1L)
            xPainter {
                paintLine = (value) % 10 == 0L
                if (paintLine) {
                    label = labelFormatter.format(Instant.ofEpochSecond(value))
                }
            }
        }
        dataset {
            lineColor = JBColor.BLUE
            fillColor = JBColor.BLUE.transparent(0.5)
            overlays = listOf(
                TitleOverlay("CPU")
            )
        }
        borderPainted = true
        margins = JBUI.insets(28, 15, 25, 15)
    }

    private val memoryChart = lineChart<Long, Double> {
        ranges {
            yMin = 0.0
            yMax = 100.0
        }
        grid {
            yLines = generator(25.0)
            xLines = generator(1L)
            xPainter {
                paintLine = (value) % 10 == 0L
                if (paintLine) {
                    label = labelFormatter.format(Instant.ofEpochSecond(value))
                }
            }
        }
        dataset {
            lineColor = JBColor.ORANGE
            fillColor = JBColor.ORANGE.transparent(0.5)
            overlays = listOf(
                TitleOverlay("Memory")
            )
        }
        borderPainted = true
        margins = JBUI.insets(28, 15, 25, 15)
    }

    init {
        val splitter = OnePixelSplitter(false).apply {
            firstComponent = cpuChart.component
            secondComponent = memoryChart.component
            setResizeEnabled(false)
        }
        add(splitter)
    }

    fun update(chartValue: ChartValue) {
        val timestamp = chartValue.timeStamp

        if (chartValue.type == ChartValueType.Cpu) {
            cpuChart.add(timestamp, chartValue.value * 100)
            cpuChart.ranges.xMin = timestamp - timeRange
            cpuChart.ranges.xMax = timestamp
        }

        if (chartValue.type == ChartValueType.Memory) {
            memoryChart.add(timestamp, chartValue.value * 100)
            memoryChart.ranges.xMin = timestamp - timeRange
            memoryChart.ranges.xMax = timestamp
        }

        repaint()
    }

    private class TitleOverlay(private val title: String) : Overlay<LineChart<*, *, *>>() {
        override fun paintComponent(g: Graphics2D) {
            g.color = JBColor.foreground()
            g.drawString(title, chart.margins.left, 20)
        }
    }
}