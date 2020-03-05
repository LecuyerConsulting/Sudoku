package com.lconsulting.sudoku.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

sealed class SudokuState {
    class SuccesSudokuState(val solution: Array<MutableSet<Int>>) : SudokuState()
    object ErrorSudokuState : SudokuState()
}

class MainViewModel : ViewModel() {

    val state = MutableLiveData<SudokuState>()

    private val sizeSudoku: Int = 9
    private val sizeGrid: Int = 3
    private lateinit var sudoku: IntArray
    private lateinit var solution: Array<MutableSet<Int>>

    fun resetSudoku() {
        sudoku = IntArray(81) { 0 }
        solution = Array(81, { i -> mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9) })
    }

    fun checkValue(value: String, g: Int, p: Int) {
        val pos =
            (sizeGrid * (g % sizeGrid) + (p % sizeGrid)) + (((p / sizeGrid) + (g / sizeGrid) * sizeGrid) * sizeSudoku)

        val fillSquare = sudoku[pos] == 0

        if (fillSquare) {
            updateSolution(value.toInt(), pos, g, p)
            state.postValue(SudokuState.SuccesSudokuState(solution))
        } else {
            state.postValue(SudokuState.ErrorSudokuState)
        }
    }

    private fun updateSolution(value: Int, pos: Int, g: Int, p: Int) {
        sudoku[pos] = value
        solution[pos] = mutableSetOf(value)
        updateLine(value, pos)
        updateColumn(value, pos)
        updateGrid(value, pos)
    }

    private fun updateLine(value: Int, pos: Int) {
        val startIndice = (pos / sizeSudoku) * sizeSudoku
        val endIndice = startIndice + sizeSudoku

        for (i in startIndice until endIndice) {
            if (sudoku[i] == 0) {
                solution[i].remove(value)
            }
        }
    }

    private fun updateColumn(value: Int, pos: Int) {
        val startIndice = pos % 9

        for (i in 0 until 9) {
            if (sudoku[startIndice + i * 9] == 0) {
                solution[startIndice + i * 9].remove(value)
            }
        }
    }

    private fun updateGrid(value: Int, pos: Int) {
        val column = pos % 9
        val row = pos % 9
        val startIndice = (column / 3) * 3 + ((row / 3) * 3) * 9
        for (i in 0 until 9) {
            val position = startIndice + (i % 3) + ((i / 3) * 9)
            if (sudoku[position] == 0) {
                solution[position].remove(value)
            }
        }
    }

}
