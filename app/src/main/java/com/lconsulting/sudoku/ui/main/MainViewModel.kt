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
        solution = Array(81) { mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9) }
        state.postValue(SudokuState.SuccesSudokuState(solution))
    }

    fun checkValue(value: String, g: Int, p: Int) {
        val pos =
            (3 * (g % 3) + (p % 3)) + (((p / 3) + (g / 3) * 3) * 9)

        val fillSquare = sudoku[pos] == 0

        if (fillSquare) {
            update(value.toInt(), pos)
            var result = true
            while (result) {
                result = launchAlgo()
            }
            state.postValue(SudokuState.SuccesSudokuState(solution))
        } else {
            state.postValue(SudokuState.ErrorSudokuState)
        }
    }

    private fun launchAlgo(): Boolean {
        if (checkOneValueBySquare()) {
            return true
        }
        if (checkOneValue9Time(::getPositionForRowBy9, ::getPositionForRow, ::findPositionByRow)) {
            return true
        }
        if (checkOneValue9Time(::getPositionForColumnBy9, ::getPositionForColumn, ::findPositionByColumn)) {
            return true
        }
        if (checkOneValue9Time(::getPositionForGridBy9, ::getPositionForGrid, ::findPositionByGrid)) {
            return true
        }
        return false
    }

    private fun update(value: Int, pos: Int) {
        sudoku[pos] = value
        solution[pos] = mutableSetOf(value)
        updateSolution(value, getIndiceForColumn(pos), ::getPositionForColumn)
        updateSolution(value, getIndiceForRow(pos), ::getPositionForRow)
        updateSolution(value, getIndiceForGrid(pos), ::getPositionForGrid)
    }

    private fun updateSolution(
        value: Int,
        startIndice: Int,
        getPosition: (start: Int, index: Int) -> Int
    ) {
        for (i in 0 until 9) {
            val position = getPosition(startIndice, i)
            if (sudoku[position] == 0) {
                solution[position].remove(value)
            }
        }
    }

    private fun checkOneValueBySquare(): Boolean {
        var result = false
        for (i in solution.indices) {
            if (sudoku[i] == 0 && solution[i].size == 1) {
                update(solution[i].toList()[0], i)
                return true
            }
        }
        return result
    }

    private fun checkOneValue9Time(
        getPositionBy9: (i: Int) -> Int,
        getPosition: (start: Int, index: Int) -> Int,
        findPosition: (start: Int, value: Int) -> Int
    ): Boolean {
        for (i in 0 until 9) {
            if (checkOneValue(getPositionBy9(i), getPosition, findPosition)) {
                return true
            }
        }
        return false
    }

    private fun checkOneValue(
        startIndice: Int,
        getPosition: (start: Int, index: Int) -> Int,
        findPosition: (start: Int, value: Int) -> Int
    ): Boolean {
        val tabCompteur = IntArray(9) { 0 }

        for (i in 0 until 9) {
            var position = getPosition(startIndice, i)
            if (sudoku[position] == 0) {
                solution[position].forEach {
                    tabCompteur[it - 1] = tabCompteur[it - 1] + 1
                }
            }
        }

        for (i in tabCompteur.indices) {
            if (tabCompteur[i] == 1) {
                val position = findPosition(startIndice, i + 1)
                update(i + 1, position)
                return true
            }
        }

        return false
    }

    private fun findPositionByRow(startIndice: Int, value: Int): Int {
        for (i in 0 until 9) {
            var position = startIndice + i
            if (solution[position].contains(value)) {
                return position
            }
        }
        return -1
    }

    private fun findPositionByColumn(startIndice: Int, value: Int): Int {
        for (i in 0 until 9) {
            var position = startIndice + i * 9
            if (solution[position].contains(value)) {
                return position
            }
        }
        return -1
    }

    private fun findPositionByGrid(startIndice: Int, value: Int): Int {
        for (i in 0 until 9) {
            var position = startIndice + (i % 3) + ((i / 3) * 9)
            if (solution[position].contains(value)) {
                return position
            }
        }
        return -1
    }

    private fun getPositionForColumnBy9(i: Int): Int = i

    private fun getPositionForGridBy9(i: Int): Int = (3 * i) + (9 * 2 * (i / 3))

    private fun getPositionForRowBy9(i: Int): Int = i * 9

    private fun getPositionForRow(startIndice: Int, i: Int): Int = startIndice + i

    private fun getPositionForColumn(startIndice: Int, i: Int): Int = startIndice + i * 9

    private fun getPositionForGrid(startIndice: Int, i: Int): Int =
        startIndice + (i % 3) + ((i / 3) * 9)

    private fun getIndiceForRow(pos: Int) = (pos / 9) * 9

    private fun getIndiceForColumn(pos: Int) = pos % 9

    private fun getIndiceForGrid(pos: Int): Int {
        val column = pos % 9
        val row = pos / 9
        return (column / 3) * 3 + ((row / 3) * 3) * 9
    }

}
