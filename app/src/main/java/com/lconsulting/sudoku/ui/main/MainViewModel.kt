package com.lconsulting.sudoku.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

sealed class SudokuState {
    class SuccesSudokuState(val value: String) : SudokuState()
    object ErrorSudokuState : SudokuState()
}

class MainViewModel : ViewModel() {

    val state = MutableLiveData<SudokuState>()

    private val sSudoku: Int = 9
    private val sGrid: Int = 3
    private lateinit var sudoku: IntArray
    private lateinit var solution: Array<IntArray>

    fun resetSudoku() {
        sudoku = IntArray(81) { 0 }
        solution = Array(81, { i -> IntArray(sSudoku, { j -> j + 1 }) })
    }

    fun checkValue(value: String, g: Int, p: Int) {
        val pos =
            (sGrid * (g % sGrid) + (p % sGrid)) + (((p / sGrid) + (g / sGrid) * sGrid) * sSudoku)

        val fillSquare = sudoku[pos] == 0

        if (fillSquare) {
            sudoku[pos] = value.toInt()
            state.postValue(SudokuState.SuccesSudokuState(value))
        } else {
            state.postValue(SudokuState.ErrorSudokuState)
        }
    }

}
