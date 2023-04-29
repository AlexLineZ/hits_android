package com.example.hits_android

import androidx.compose.ui.graphics.Color

class Block(n : String, c: Color, offset: Double) {
    private var name: String
    private var color: Color
    private var yPos: Double
    private var xPos: Double
    private var yOffset: Double

    init {
        name = n
        color = c
        yPos = 0.0
        xPos = 0.0
        yOffset = offset
    }

    fun getName() : String {
        return name
    }

    fun getColor() : Color {
        return color
    }

    fun getHeight() : Double {
        return yPos
    }

    fun changePosition(x : Double, y: Double) {
        xPos = x
        yPos = y + yOffset
    }
}