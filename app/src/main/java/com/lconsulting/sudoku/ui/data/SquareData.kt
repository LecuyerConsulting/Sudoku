package com.lconsulting.sudoku.ui.data

import com.lconsulting.sudoku.R

data class SquareData(
    var value: Int = 0,
    var textColor: Int = R.color.colorValue,
    val possibility: MutableSet<Int> = mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
)