package com.example.fitnessapp.util.functions

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

fun createImageVector(): ImageVector{
    return ImageVector.Builder(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportHeight = 24f,
        viewportWidth = 24f
    ).run {
        addPath(
        pathData = addPathNodes("M216-570q36 0 70 13.5t61 38.5l395 378h18q25.5 0 42.75-17.25T820-200q0-11.2-2.125-23.8Q815.75-236.4 803-249L623-429l-76-227-92 23q-29 7-52-10.5T380-691v-97l-54-27-175 235q-2 2-3.5 4.5T144-570h72Zm0 60h-78q3 11 9 20.176 6 9.177 14 16.824l338 307q14 13 31 19.5t36 6.5h89L306-474q-18-17-41.455-26.5Q241.091-510 216-510ZM566-80q-30 0-57-11t-50-31L134-417q-46-42-51.5-103T114-631l154-206q17-23 45.5-30.5T368-861l28 14q21 11 32.5 30t11.5 42v84l74-19q30-8 58 7.5t38 44.5l65 196 170 170q20 20 27.5 43t7.5 49q0 50-35 85t-85 35H566Z"),
        name = "",
        fill = SolidColor(Color.White),
        stroke = SolidColor(Color.Black)
        )
        build()
    }
}