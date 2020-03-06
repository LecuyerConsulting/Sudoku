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
            var result = true
            while (result) {
                result = launcAlgo()
            }
            state.postValue(SudokuState.SuccesSudokuState(solution))
        } else {
            state.postValue(SudokuState.ErrorSudokuState)
        }
    }

    private fun launcAlgo(): Boolean {
        if (checkOneValueBySquare()) {
            return true
        }
        if (checkOneValueByRow9Time()) {
            return true
        }
        if (checkOneValueByColumn9Time()) {
            return true
        }
        if (checkOneValueByGrid9Time()) {
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

    private fun updateSolution(value: Int, startIndice: Int, getPosition: (start: Int, index: Int) -> Int) {
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

    private fun checkOneValueByRow9Time(): Boolean {
        for (i in 0 until 9) {
            if (checkOneValueByRow(i * 9)) {
                return true
            }
        }
        return false
    }

    private fun checkOneValueByRow(startIndice: Int): Boolean {
        val endIndice = startIndice + 9
        val tabCompteur = IntArray(9) { 0 }

        for (i in startIndice until endIndice) {
            if (sudoku[i] == 0) {
                solution[i].forEach {
                    tabCompteur[it - 1] = tabCompteur[it - 1] + 1
                }
            }
        }

        for (i in tabCompteur.indices) {
            if (tabCompteur[i] == 1) {
                val position = findPositionByRow(startIndice, endIndice, i + 1)
                update(i + 1, position)
                return true
            }
        }

        return false
    }

    private fun findPositionByRow(startIndice: Int, endIndice: Int, value: Int): Int {
        for (i in startIndice until endIndice) {
            if (solution[i].contains(value)) {
                return i
            }
        }
        return -1
    }

    private fun checkOneValueByColumn9Time(): Boolean {
        for (i in 0 until 9) {
            if (checkOneValueByColumn(i)) {
                return true
            }
        }
        return false
    }

    private fun checkOneValueByColumn(startIndice: Int): Boolean {
        val tabCompteur = IntArray(9) { 0 }

        for (i in 0 until 9) {
            var position = startIndice + i * 9
            if (sudoku[position] == 0) {
                solution[position].forEach {
                    tabCompteur[it - 1] = tabCompteur[it - 1] + 1
                }
            }
        }

        for (i in tabCompteur.indices) {
            if (tabCompteur[i] == 1) {
                val position = findPositionByColumn(startIndice, i + 1)
                update(i + 1, position)
                return true
            }
        }

        return false
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

    private fun checkOneValueByGrid9Time(): Boolean {
        for (i in 0 until 9) {
            val startIndice = (3 * i) + (9 * 2 * (i / 3))
            if (checkOneValueByGrid(startIndice)) {
                return true
            }
        }
        return false
    }

    private fun checkOneValueByGrid(startIndice: Int): Boolean {
        val tabCompteur = IntArray(9) { 0 }

        for (i in 0 until 9) {
            var position = startIndice + (i % 3) + ((i / 3) * 9)
            if (sudoku[position] == 0) {
                solution[position].forEach {
                    tabCompteur[it - 1] = tabCompteur[it - 1] + 1
                }
            }
        }

        for (i in tabCompteur.indices) {
            if (tabCompteur[i] == 1) {
                val position = findPositionByGrid(startIndice, i + 1)
                update(i + 1, position)
                return true
            }
        }

        return false
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
