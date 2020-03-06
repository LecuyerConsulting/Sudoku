package com.lconsulting.sudoku.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

sealed class SudokuState {
    class SuccesSudokuState(val solution: Array<MutableSet<Int>>) : SudokuState()
    object ErrorSudokuState : SudokuState()
}

class MainViewModel : ViewModel() {

    val state = MutableLiveData<SudokuState>()

    private lateinit var sudoku: IntArray
    private lateinit var solution: Array<MutableSet<Int>>

    fun resetSudoku() {
        sudoku = IntArray(81) { 0 }
        solution = Array(81, { i -> mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9) })
    }

    fun checkValue(value: String, g: Int, p: Int) {
        val pos =
            (3 * (g % 3) + (p % 3)) + (((p / 3) + (g / 3) * 3) * 9)

        val fillSquare = sudoku[pos] == 0

        if (fillSquare) {
            update(value.toInt(), pos)
            launcAlgo()
            state.postValue(SudokuState.SuccesSudokuState(solution))
        } else {
            state.postValue(SudokuState.ErrorSudokuState)
        }
    }

    private fun launcAlgo() {
        var findValue = true
        while (findValue) {
            findValue = checkOneValueBySquare()
        }
    }

    private fun update(value: Int, pos: Int) {
        updateSudoku(value, pos)
        updateSolution(value, pos)
    }

    private fun updateSolution(value: Int, pos: Int) {
        solution[pos] = mutableSetOf(value)
        updateRow(value, pos)
        updateColumn(value, pos)
        updateGrid(value, pos)
    }

    private fun updateSudoku(value: Int, pos: Int) {
        sudoku[pos] = value
    }

    private fun checkOneValueBySquare(): Boolean {
        var result = false
        for (i in solution.indices) {
            if (sudoku[i] == 0 && solution[i].size == 1) {
                update(solution[i].toList()[0], i)
                result = true
            }
        }
        return result
    }

    private fun updateRow(value: Int, pos: Int) {
        val startIndice = getIndiceForRow(pos)
        val endIndice = startIndice + 9

        for (i in startIndice until endIndice) {
            if (sudoku[i] == 0) {
                solution[i].remove(value)
            }
        }
    }

    private fun updateColumn(value: Int, pos: Int) {
        val startIndice = getIndiceForColumn(pos)
        for (i in 0 until 9) {
            if (sudoku[startIndice + i * 9] == 0) {
                solution[startIndice + i * 9].remove(value)
            }
        }
    }

    private fun updateGrid(value: Int, pos: Int) {
        val startIndice  = getIndiceForGrid(pos)
        for (i in 0 until 9) {
            val position = startIndice + (i % 3) + ((i / 3) * 9)
            if (sudoku[position] == 0) {
                solution[position].remove(value)
            }
        }
    }

    private fun getIndiceForRow(pos : Int) = (pos / 9) * 9

    private fun getIndiceForColumn(pos : Int) =  pos % 9

    private fun getIndiceForGrid(pos : Int) : Int{
        val column = pos % 9
        val row = pos / 9
        return (column / 3) * 3 + ((row / 3) * 3) * 9
    }

}
