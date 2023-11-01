@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.diagnosticsclientplugin.toolWindow.components

import com.github.rafaelldi.diagnosticsclientplugin.DiagnosticsClientBundle
import com.github.rafaelldi.diagnosticsclientplugin.model.ChartEvent
import com.github.rafaelldi.diagnosticsclientplugin.model.ChartEventType
import com.intellij.ui.JBColor
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.charts.*
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Paint
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CounterChartPanel : BorderLayoutPanel() {
    companion object {
        private const val TIME_RANGE = 60
        private val labelFormatter = DateTimeFormatter
            .ofPattern("HH:mm:ss")
            .withZone(ZoneId.systemDefault())

        private fun createTimeLabel(time: Long) = labelFormatter.format(Instant.ofEpochSecond(time))
        private fun createValueLabel(value: Double, label: String) = "${String.format("%.2f", value)} $label"

        private fun getXByLocation(location: Point, chart: GridChartWrapper<Long, Double>): Long? {
            val minMax = chart.findMinMax()
            if (!minMax.isInitialized) return null

            if (location.x < chart.margins.left) return null
            if (location.x > chart.width - chart.margins.right) return null

            val chartX = location.x - chart.margins.left
            val chartWidth = chart.width - (chart.margins.left + chart.margins.right)
            val xWidth = minMax.xMax - minMax.xMin
            return (chartX * xWidth) / chartWidth + minMax.xMin
        }

        private fun drawOvalPoint(point: Point2D.Double, g: Graphics2D, fillColor: Paint?, drawColor: Paint?) {
            val radius = 4
            val x = point.x.toInt() - radius
            val y = point.y.toInt() - radius
            val width = radius * 2
            val height = radius * 2

            g.paint = fillColor
            g.fillOval(x, y, width, height)

            g.paint = drawColor
            g.drawOval(x, y, width, height)
        }

        private fun drawRectPoint(point: Point2D.Double, g: Graphics2D, fillColor: Paint?, drawColor: Paint?) {
            val radius = 4
            val x = point.x.toInt() - radius
            val y = point.y.toInt() - radius
            val width = radius * 2
            val height = radius * 2

            g.paint = fillColor
            g.fillRect(x, y, width, height)

            g.paint = drawColor
            g.drawRect(x, y, width, height)
        }

        private fun drawDataLabels(
            point: Point2D.Double,
            valueLabel: String,
            timeLabel: String?,
            g: Graphics2D,
            chart: GridChartWrapper<Long, Double>
        ) {
            g.color = JBColor.foreground()

            val valueLabelBounds = g.fontMetrics.getStringBounds(valueLabel, null)
            val valueLabelX = point.x.toInt() - valueLabelBounds.width.toInt() / 2
            var valueLabelY = point.y.toInt() - valueLabelBounds.height.toInt()
            if (valueLabelY < chart.margins.top + valueLabelBounds.height.toInt())
                valueLabelY = chart.margins.top + valueLabelBounds.height.toInt()
            g.drawString(valueLabel, valueLabelX, valueLabelY)

            if (timeLabel != null) {
                val timeLabelBounds = g.fontMetrics.getStringBounds(timeLabel, null)
                val timeLabelX = point.x.toInt() - timeLabelBounds.width.toInt() / 2
                val timeLabelY = chart.height - chart.margins.bottom + timeLabelBounds.height.toInt()
                g.paint = chart.gridLabelColor
                g.drawString(timeLabel, timeLabelX, timeLabelY)
            }
        }
    }

    private val valueOverlays = mutableListOf<ValueOverlay>()

    private val exceptionPoints = HashMap<Long, String>()
    private val gcPoints = HashMap<Long, String>()

    private val chartMargins = JBUI.insets(28, 15, 25, 15)

    private val cpuChart = lineChart<Long, Double> {
        ranges {
            yMin = 0.0
            yMax = 100.0
        }
        grid {
            yLines = generator(25.0)
            xLines = generator(1L)
            xPainter {
                paintLine = (value) % 60 == 0L
                val hasActiveOverlay = valueOverlays.any { it.mouseLocation != null }

                if (paintLine && !hasActiveOverlay) {
                    label = createTimeLabel(value)
                }
            }
        }
        datasets {
            dataset {
                label = "CPU"
                lineColor = JBColor.BLUE
                fillColor = JBColor.BLUE.transparent(0.5)
                val valueOverlay = ValueOverlay(this, "%", exceptionPoints)
                valueOverlays.add(valueOverlay)
                overlays = listOf(
                    TitleOverlay(DiagnosticsClientBundle.message("chart.cpu.title.overlay"), "%", this),
                    valueOverlay
                )
            }
            dataset {
                label = "Exception"
                lineColor = JBColor.background().transparent(0.0)
                overlays = listOf(PointOverlay(this, JBColor.RED, exceptionPoints))
            }
        }
        borderPainted = true
        margins = chartMargins
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
                paintLine = (value) % 60 == 0L
                val hasActiveOverlay = valueOverlays.any { it.mouseLocation != null }

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
                val valueOverlay = ValueOverlay(this, "MB", gcPoints)
                valueOverlays.add(valueOverlay)
                overlays = listOf(
                    TitleOverlay(DiagnosticsClientBundle.message("chart.memory.title.overlay"), "MB", this),
                    valueOverlay
                )
            }
            dataset {
                label = "Working Set"
                lineColor = JBColor.ORANGE
                fillColor = JBColor.ORANGE.transparent(0.5)
                val valueOverlay = ValueOverlay(this, "MB", gcPoints, false)
                valueOverlays.add(valueOverlay)
                overlays = listOf(valueOverlay)
            }
            dataset {
                label = "Gc"
                lineColor = JBColor.background().transparent(0.0)
                overlays = listOf(PointOverlay(this, JBColor.PINK, gcPoints))
            }
        }
        borderPainted = true
        margins = chartMargins
    }

    private val exceptionCountChart = lineChart<Long, Double> {
        ranges {
            yMin = 0.0
            yMax = 30.0
        }
        grid {
            yLines = generator(5.0)
            xLines = generator(1L)
            xPainter {
                paintLine = (value) % 60 == 0L
                val hasActiveOverlay = valueOverlays.any { it.mouseLocation != null }

                if (paintLine && !hasActiveOverlay) {
                    label = createTimeLabel(value)
                }
            }
        }
        datasets {
            dataset {
                label = "Number of Exceptions"
                lineColor = JBColor.PINK
                fillColor = JBColor.PINK.transparent(0.5)
                val valueOverlay = ValueOverlay(this, "")
                valueOverlays.add(valueOverlay)
                overlays = listOf(
                    TitleOverlay(DiagnosticsClientBundle.message("chart.exception.title.overlay"), "", this),
                    valueOverlay
                )
            }
        }
        borderPainted = true
        margins = chartMargins
    }

    private val threadCountChart = lineChart<Long, Double> {
        ranges {
            yMin = 0.0
            yMax = 50.0
        }
        grid {
            yLines = generator(5.0)
            xLines = generator(1L)
            xPainter {
                paintLine = (value) % 60 == 0L
                val hasActiveOverlay = valueOverlays.any { it.mouseLocation != null }

                if (paintLine && !hasActiveOverlay) {
                    label = createTimeLabel(value)
                }
            }
        }
        datasets {
            dataset {
                label = "Number of Threads"
                lineColor = JBColor.CYAN
                fillColor = JBColor.CYAN.transparent(0.5)
                val valueOverlay = ValueOverlay(this, "")
                valueOverlays.add(valueOverlay)
                overlays = listOf(
                    TitleOverlay(DiagnosticsClientBundle.message("chart.thread.title.overlay"), "", this),
                    valueOverlay
                )
            }
        }
        borderPainted = true
        margins = chartMargins
    }

    init {
        val firstSplitter = OnePixelSplitter(false).apply {
            firstComponent = cpuChart.component
            secondComponent = memoryChart.component
            setResizeEnabled(false)
        }
        val secondSplitter = OnePixelSplitter(false).apply {
            firstComponent = exceptionCountChart.component
            secondComponent = threadCountChart.component
            setResizeEnabled(false)
        }
        val mainSplitter = OnePixelSplitter(false).apply {
            firstComponent = firstSplitter
            secondComponent = secondSplitter
            setResizeEnabled(false)
        }
        add(mainSplitter)

        addChartRepaint()
    }

    private fun addChartRepaint() {
        val charts = listOf(cpuChart, memoryChart, exceptionCountChart, threadCountChart)
        for (chart in charts) {
            val adapter = ChartMouseAdapter(chart)
            for (chartToListen in charts) {
                if (chartToListen == chart) continue
                chartToListen.addChartListeners(adapter)
            }
        }
    }

    private fun XYLineChart<*, *>.addChartListeners(adapter: ChartMouseAdapter) {
        component.addMouseListener(adapter)
        component.addMouseMotionListener(adapter)
    }

    fun update(chartValue: ChartEvent) {
        val timestamp = chartValue.timeStamp

        if (chartValue.type == ChartEventType.Cpu) {
            val value = chartValue.value
            cpuChart["CPU"].add(timestamp to value)
            cpuChart.ranges.xMin = timestamp - TIME_RANGE
            cpuChart.ranges.xMax = timestamp
        }

        if (chartValue.type == ChartEventType.GcHeapSize) {
            memoryChart["GC Heap Size"].add(timestamp to chartValue.value)
            memoryChart.ranges.xMin = timestamp - TIME_RANGE
            memoryChart.ranges.xMax = timestamp
        }

        if (chartValue.type == ChartEventType.WorkingSet) {
            memoryChart["Working Set"].add(timestamp to chartValue.value)
            memoryChart.ranges.xMin = timestamp - TIME_RANGE
            memoryChart.ranges.xMax = timestamp
            memoryChart.ranges.yMax = maxOf(memoryChart.ranges.yMax, chartValue.value)
        }

        if (chartValue.type == ChartEventType.Exception) {
            exceptionPoints[timestamp] = chartValue.label ?: "Exception"
            cpuChart["Exception"].add(timestamp to 0.0)
            cpuChart.ranges.xMin = timestamp - TIME_RANGE
            cpuChart.ranges.xMax = timestamp
        }

        if (chartValue.type == ChartEventType.Gc) {
            gcPoints[timestamp] = chartValue.label ?: "GC"
            memoryChart["Gc"].add(timestamp to 0.0)
            memoryChart.ranges.xMin = timestamp - TIME_RANGE
            memoryChart.ranges.xMax = timestamp
        }

        if (chartValue.type == ChartEventType.ExceptionCount) {
            val value = chartValue.value
            exceptionCountChart.add(timestamp, value)
            exceptionCountChart.ranges.xMin = timestamp - TIME_RANGE
            exceptionCountChart.ranges.xMax = timestamp
            exceptionCountChart.ranges.yMax = maxOf(exceptionCountChart.ranges.yMax, chartValue.value)
        }

        if (chartValue.type == ChartEventType.ThreadCount) {
            val value = chartValue.value
            threadCountChart.add(timestamp, value)
            threadCountChart.ranges.xMin = timestamp - TIME_RANGE
            threadCountChart.ranges.xMax = timestamp
            threadCountChart.ranges.yMax = maxOf(threadCountChart.ranges.yMax, chartValue.value)
        }

        repaint()
    }

    private class ChartMouseAdapter(private val chartToRepaint: XYLineChart<Long, Double>) : MouseAdapter() {
        override fun mouseEntered(e: MouseEvent) {
            chartToRepaint.component.repaint()
        }

        override fun mouseExited(e: MouseEvent) {
            chartToRepaint.component.repaint()
        }

        override fun mouseMoved(e: MouseEvent) {
            chartToRepaint.component.repaint()
        }
    }

    private class TitleOverlay(
        private val title: String,
        private val label: String,
        private val xyLineDataset: XYLineDataset<Long, Double>
    ) : Overlay<LineChart<*, *, *>>() {
        override fun paintComponent(g: Graphics2D) {
            g.color = JBColor.foreground()
            g.drawString(title, chart.margins.left, 20)

            val value = xyLineDataset.data.lastOrNull()?.y ?: 0.0
            val valueLabel = createValueLabel(value, label)
            val valueLabelWidth = g.fontMetrics.stringWidth(valueLabel)
            g.drawString(valueLabel, chart.width - chart.margins.left - valueLabelWidth, 20)
        }
    }

    private inner class ValueOverlay(
        private val xyLineDataset: XYLineDataset<Long, Double>,
        private val label: String,
        private val pointSet: HashMap<Long, String>? = null,
        private val drawTimeLabel: Boolean = true
    ) : Overlay<LineChart<Long, Double, *>>() {
        override fun paintComponent(g: Graphics2D) {
            val overlayWithMouseLocation = valueOverlays.find { it.mouseLocation != null } ?: return
            val chartWithMouseLocation = overlayWithMouseLocation.chart
            val location = overlayWithMouseLocation.mouseLocation ?: return

            val x = getXByLocation(location, chartWithMouseLocation) ?: return
            if (pointSet?.contains(x) == true) return

            val minMax = chart.findMinMax()
            if (!minMax.isInitialized) return
            val dataPair = xyLineDataset.data.find { it.x == x } ?: xyLineDataset.data.first()
            val point = chart.findLocation(minMax, dataPair)

            drawOvalPoint(point, g, xyLineDataset.lineColor, chart.background)
            val valueLabel = createValueLabel(dataPair.y, label)
            val timeLabel = if (drawTimeLabel) createTimeLabel(dataPair.x) else null
            drawDataLabels(point, valueLabel, timeLabel, g, chart)
        }
    }

    private class PointOverlay(
        private val xyLineDataset: XYLineDataset<Long, Double>,
        private val color: Color,
        private val pointSet: HashMap<Long, String>
    ) : Overlay<LineChart<Long, Double, *>>() {
        override fun paintComponent(g: Graphics2D) {
            val minMax = chart.findMinMax()
            if (!minMax.isInitialized) return

            for (dataPair in xyLineDataset.data.filter { it.x >= minMax.xMin }) {
                val point = chart.findLocation(minMax, dataPair)
                drawRectPoint(point, g, color, chart.background)
            }

            val location = mouseLocation ?: return
            val x = getXByLocation(location, chart) ?: return
            val label = pointSet[x] ?: return
            val dataPair = xyLineDataset.data.find { it.x == x } ?: return
            val point = chart.findLocation(minMax, dataPair)
            val timeLabel = createTimeLabel(dataPair.x)
            drawDataLabels(point, label, timeLabel, g, chart)
        }
    }
}