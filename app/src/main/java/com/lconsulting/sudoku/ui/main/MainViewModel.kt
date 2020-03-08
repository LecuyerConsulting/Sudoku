package com.lconsulting.sudoku.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.ui.data.SquareData


sealed class SudokuState {
    class FillSquareSudokuState(
        val solution: Array<SquareData>,
        val idRes: Int,
        val value: Int
    ) : SudokuState()

    class FillSquareAlgoSudokuState(
        val solution: Array<SquareData>,
        val idRes: Int,
        val value: Int,
        val idGrid: Int,
        val idSquare: Int
    ) : SudokuState()

    class DisplayButtonState(val possibility: MutableSet<Int>) : SudokuState()
    class ResetSudokuState(val solution: Array<SquareData>) : SudokuState()
    object InsertValueSudokuState : SudokuState()
    object ErrorSudokuState : SudokuState()
}

class MainViewModel : ViewModel() {

    val state = MutableLiveData<SudokuState>()

    private lateinit var solution: Array<SquareData>

    fun resetSudoku() {
        solution = Array(81) { SquareData() }
        state.postValue(SudokuState.ResetSudokuState(solution))
    }

    fun getNumberAvailable(g: Int, p: Int) {
        val pos = getPosition(g, p)

        val squareData = solution[pos]
        state.postValue(SudokuState.DisplayButtonState(squareData.possibility))
    }

    fun checkValue(sValue: String, g: Int, p: Int) {
        val pos = getPosition(g, p)

        val newValue = sValue.toInt()
        val oldValue = solution[pos].value

        if (oldValue != 0) {
            updateSolution(oldValue, getIndiceForColumn(pos), ::getPositionForColumn, ::add)
            updateSolution(oldValue, getIndiceForRow(pos), ::getPositionForRow, ::add)
            updateSolution(oldValue, getIndiceForGrid(pos), ::getPositionForGrid, ::add)
            solution[pos].value = 0
        }

        update(newValue, R.color.colorValue, pos)

        state.postValue(
            SudokuState.FillSquareSudokuState(
                solution,
                R.string.insert_value,
                newValue
            )
        )
    }

    fun startAlgo() {
        if (!launchAlgo()) {
            state.postValue(SudokuState.ErrorSudokuState)
        }
    }

    private fun launchAlgo(): Boolean {
        if (checkOneValueBySquare()) {
            return true
        }
        if (checkOneValue9Time(
                ::getPositionForGridBy9,
                ::getPositionForGrid,
                ::findPositionByGrid,
                R.string.one_value_by_grid
            )
        ) {
            return true
        }
        if (checkOneValue9Time(
                ::getPositionForRowBy9,
                ::getPositionForRow,
                ::findPositionByRow,
                R.string.one_value_by_row
            )
        ) {
            return true
        }
        if (checkOneValue9Time(
                ::getPositionForColumnBy9,
                ::getPositionForColumn,
                ::findPositionByColumn,
                R.string.one_value_by_column
            )
        ) {
            return true
        }
        return false
    }

    private fun update(value: Int, textColor: Int, pos: Int) {
        solution[pos].let {
            it.value = value
            it.textColor = textColor
        }

        updateSolution(value, getIndiceForGrid(pos), ::getPositionForGrid, ::remove)
        updateSolution(value, getIndiceForRow(pos), ::getPositionForRow, ::remove)
        updateSolution(value, getIndiceForColumn(pos), ::getPositionForColumn, ::remove)
    }

    private fun updateSolution(
        value: Int,
        startIndice: Int,
        getPosition: (start: Int, index: Int) -> Int,
        action: (squareData: SquareData, value: Int) -> Unit
    ) {
        for (i in 0 until 9) {
            val position = getPosition(startIndice, i)
            if (solution[position].value != value) {
                action(solution[position], value)
            }
        }
    }

    private fun remove(squareData: SquareData, value: Int) {
        squareData.possibility.remove(value)
    }

    private fun add(squareData: SquareData, value: Int) {
        squareData.possibility.add(value)
    }

    private fun checkOneValueBySquare(): Boolean {
        var result = false
        for (i in solution.indices) {
            if (solution[i].value == 0 && solution[i].possibility.size == 1) {
                val value = solution[i].possibility.toList()[0]
                update(value, R.color.colorValueFound, i)

                state.postValue(
                    SudokuState.FillSquareAlgoSudokuState(
                        solution,
                        R.string.one_value_by_square,
                        value,
                        getIdGrid(i),
                        getIdSquareInGrid(i)
                    )
                )
                return true
            }
        }
        return result
    }

    private fun checkOneValue9Time(
        getPositionBy9: (i: Int) -> Int,
        getPosition: (start: Int, index: Int) -> Int,
        findPosition: (start: Int, value: Int) -> Int,
        idRessource: Int
    ): Boolean {
        for (i in 0 until 9) {
            if (checkOneValue(getPositionBy9(i), getPosition, findPosition, idRessource)) {
                return true
            }
        }
        return false
    }

    private fun checkOneValue(
        startIndice: Int,
        getPosition: (start: Int, index: Int) -> Int,
        findPosition: (start: Int, value: Int) -> Int,
        idRessource: Int
    ): Boolean {
        val tabCompteur = IntArray(9) { 0 }

        for (i in 0 until 9) {
            var position = getPosition(startIndice, i)
            if (solution[position].value == 0) {
                solution[position].possibility.forEach {
                    tabCompteur[it - 1] = tabCompteur[it - 1] + 1
                }
            }
        }

        for (i in tabCompteur.indices) {
            if (tabCompteur[i] == 1) {
                val value = i + 1
                val position = findPosition(startIndice, value)
                update(value, R.color.colorValueFound, position)

                Log.d("tom971", "checkOneValue $position")

                state.postValue(
                    SudokuState.FillSquareAlgoSudokuState(
                        solution,
                        idRessource,
                        value,
                        getIdGrid(position),
                        getIdSquareInGrid(position)
                    )
                )
                return true
            }
        }

        return false
    }

    private fun findPositionByRow(startIndice: Int, value: Int): Int {
        for (i in 0 until 9) {
            var position = startIndice + i
            val squareData = solution[position]
            if (squareData.value == 0 && squareData.possibility.contains(value)) {
                return position
            }
        }
        return -1
    }

    private fun findPositionByColumn(startIndice: Int, value: Int): Int {
        for (i in 0 until 9) {
            var position = startIndice + i * 9
            val squareData = solution[position]
            if (squareData.value == 0 && squareData.possibility.contains(value)) {
                return position
            }
        }
        return -1
    }

    private fun findPositionByGrid(startIndice: Int, value: Int): Int {
        for (i in 0 until 9) {
            var position = startIndice + (i % 3) + ((i / 3) * 9)
            val squareData = solution[position]
            if (squareData.value == 0 && squareData.possibility.contains(value)) {
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

    private fun getPosition(g: Int, p: Int) =
        (3 * (g % 3) + (p % 3)) + (((p / 3) + (g / 3) * 3) * 9)

    private fun getIdGrid(position: Int): Int {
        val indiceGrid = getIndiceForGrid(position)

        return ((indiceGrid % 9) / 3) + (indiceGrid / 9)
    }

    private fun getIdSquareInGrid(position: Int): Int {
        val indiceRow = getIndiceForRow(position)
        val indiceColumn = getIndiceForColumn(position)

        return (((indiceRow / 9) % 3) * 3) + (indiceColumn % 3)
    }
}
