@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.generated.ChartValue
import com.github.rafaelldi.diagnosticsclientplugin.generated.ChartValueType
import com.intellij.ui.JBColor
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.charts.*
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Graphics2D
import java.awt.geom.Point2D
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CounterChartPanel : BorderLayoutPanel() {
    companion object {
        private const val timeRange = 60
        private val labelFormatter = DateTimeFormatter
            .ofPattern("HH:mm:ss")
            .withZone(ZoneId.systemDefault())

        private fun createTimeLabel(time: Long) = labelFormatter.format(Instant.ofEpochSecond(time))
        private fun createValueLabel(value: Double, label: String) = "${String.format("%.2f", value)} $label"
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
                val hasActiveOverlay = this@lineChart.overlays
                    .filterIsInstance<ValueOverlay>()
                    .any { it.mouseLocation != null }

                if (paintLine && !hasActiveOverlay) {
                    label = createTimeLabel(value)
                }
            }
        }
        dataset {
            lineColor = JBColor.BLUE
            fillColor = JBColor.BLUE.transparent(0.5)
            overlays = listOf(
                TitleOverlay(DiagnosticsClientBundle.message("chart.cpu.title.overlay"), "%", this),
                ValueOverlay(this, "%")
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
            yLines = generator(200.0)
            xLines = generator(1L)
            xPainter {
                paintLine = (value) % 10 == 0L
                val hasActiveOverlay = this@lineChart.overlays
                    .filterIsInstance<ValueOverlay>()
                    .any { it.mouseLocation != null }

                if (paintLine && !hasActiveOverlay) {
                    label = createTimeLabel(value)
                }
            }
        }
        datasets {
            dataset {
                label = "GC Heap Size"
                lineColor = JBColor.RED
                fillColor = JBColor.RED.transparent(0.5)
                stacked = true
                overlays = listOf(
                    TitleOverlay(DiagnosticsClientBundle.message("chart.memory.title.overlay"), "MB", this),
                    ValueOverlay(this, "MB")
                )
            }
            dataset {
                label = "Working Set"
                lineColor = JBColor.ORANGE
                fillColor = JBColor.ORANGE.transparent(0.5)
                overlays = listOf(ValueOverlay(this, "MB", false))
            }
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
            val value = chartValue.value
            cpuChart.add(timestamp, value)
            cpuChart.ranges.xMin = timestamp - timeRange
            cpuChart.ranges.xMax = timestamp
        }

        if (chartValue.type == ChartValueType.GcHeapSize) {
            memoryChart["GC Heap Size"].add(timestamp to chartValue.value)
            memoryChart.ranges.xMin = timestamp - timeRange
            memoryChart.ranges.xMax = timestamp
        }

        if (chartValue.type == ChartValueType.WorkingSet) {
            memoryChart["Working Set"].add(timestamp to chartValue.value)
            memoryChart.ranges.xMin = timestamp - timeRange
            memoryChart.ranges.xMax = timestamp
            memoryChart.ranges.yMax = maxOf(memoryChart.ranges.yMax, chartValue.value)
        }

        repaint()
    }

    private class TitleOverlay(
        private val title: String,
        private val label: String,
        private val xyLineDataset: XYLineDataset<Long, Double>
    ) : Overlay<LineChart<*, *, *>>() {
        override fun paintComponent(g: Graphics2D) {
            g.color = JBColor.foreground()
            g.drawString(title, chart.margins.left, 20)

            val value = xyLineDataset.data.last().y
            val valueLabel = createValueLabel(value, label)
            val valueLabelWidth = g.fontMetrics.stringWidth(valueLabel)
            g.drawString(valueLabel, chart.width - chart.margins.left - valueLabelWidth, 20)
        }
    }

    private class ValueOverlay(
        private val xyLineDataset: XYLineDataset<Long, Double>,
        private val label: String,
        private val drawTimeLabel: Boolean = true
    ) :
        Overlay<LineChart<Long, Double, *>>() {
        override fun paintComponent(g: Graphics2D) {
            val location = mouseLocation ?: return
            val minMax = chart.findMinMax()
            if (!minMax.isInitialized) return

            val chartX = location.x - chart.margins.left
            val chartWidth = chart.width - (chart.margins.left + chart.margins.right)
            val xWidth = minMax.xMax - minMax.xMin
            val x = (chartX * xWidth) / chartWidth + minMax.xMin
            val dataPair = xyLineDataset.data.find { it.x == x } ?: xyLineDataset.data.first()
            val point = chart.findLocation(minMax, dataPair)

            drawDataPoint(point, g)
            drawDataLabels(point, dataPair, label, g)
        }

        private fun drawDataPoint(point: Point2D.Double, g: Graphics2D) {
            val radius = 4
            val x = point.x.toInt() - radius
            val y = point.y.toInt() - radius
            val width = radius * 2
            val height = radius * 2

            g.paint = xyLineDataset.lineColor
            g.fillOval(x, y, width, height)

            g.paint = chart.background
            g.drawOval(x, y, width, height)
        }

        private fun drawDataLabels(
            point: Point2D.Double,
            dataPair: Coordinates<Long, Double>,
            label: String,
            g: Graphics2D
        ) {
            g.color = JBColor.foreground()

            val valueLabel = createValueLabel(dataPair.y, label)
            val valueLabelBounds = g.fontMetrics.getStringBounds(valueLabel, null)
            val valueLabelX = point.x.toInt() - valueLabelBounds.width.toInt() / 2
            var valueLabelY = point.y.toInt() - valueLabelBounds.height.toInt()
            if (valueLabelY < chart.margins.top + valueLabelBounds.height.toInt())
                valueLabelY = chart.margins.top + valueLabelBounds.height.toInt()
            g.drawString(valueLabel, valueLabelX, valueLabelY)

            if (drawTimeLabel) {
                val timeLabel = createTimeLabel(dataPair.x)
                val timeLabelBounds = g.fontMetrics.getStringBounds(timeLabel, null)
                val timeLabelX = point.x.toInt() - timeLabelBounds.width.toInt() / 2
                val timeLabelY = chart.height - chart.margins.bottom + timeLabelBounds.height.toInt()
                g.paint = chart.gridLabelColor
                g.drawString(timeLabel, timeLabelX, timeLabelY)
            }
        }
    }
}