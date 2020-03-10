package com.lconsulting.sudoku.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.data.SquareData


sealed class SudokuState {
    class FillSquare(
        val sudoku: Array<SquareData>, val idRes: Int, val value: Int
    ) : SudokuState()

    class FillSquareAlgo(
        val sudoku: Array<SquareData>,
        val idRes: Int,
        val value: Int,
        val idGrid: Int,
        val idSquare: Int
    ) : SudokuState()

    class PairAlgo(
        val sudoku: Array<SquareData>,
        val idRes: Int,
        val listSquareSelected: List<Pair<Int, Int>>,
        val listValueSelected: Set<Int>
    ) : SudokuState()

    class DisplayButton(val possibility: MutableSet<Int>) : SudokuState()
    class Reset(val solution: Array<SquareData>) : SudokuState()
    object InsertValue : SudokuState()
    class DisplayMessage(val idResString: Int) : SudokuState()
}

/**
 *
 * Sudoku 9x9
 * Grid 3x3
 * Index in Sudoku in [0, 80]
 *
 * ||00|01|02||03|04|05||06|07|08||
 * ||09|10|11||12|13|14||15|16|17||
 * ||18|19|20||21|22|23||24|25|26||
 * --------------------------------
 * ||27|28|29||30|31|32||33|34|35||
 * ||36|37|38||39|40|41||42|43|44||
 * ||45|46|47||48|49|50||51|52|53||
 * --------------------------------
 * ||54|55|56||57|58|59||60|61|62||
 * ||63|64|65||66|67|68||69|70|71||
 * ||72|73|74||75|76|77||78|79|80||
 *
 */

open class SudokuViewModel : ViewModel() {

    val state = MutableLiveData<SudokuState>()

    private var digitsToFind = 81

    private var sudoku: Array<SquareData> = Array(81) { SquareData() }

    /**
     * reset sudoku to default values
     */
    fun reset() {
        digitsToFind = 81
        sudoku = Array(81) { SquareData() }
        state.postValue(SudokuState.Reset(sudoku))
    }

    /**
     * get digits available for the selected square
     *
     * @param idGrid toto
     * @param idSquare tata
     */
    fun getDigitAvailable(idGrid: Int, idSquare: Int) {
        val pos = getIndex(idGrid, idSquare)

        val squareData = sudoku[pos]
        state.postValue(SudokuState.DisplayButton(squareData.possibility))
    }

    /**
     * if there already is a value in the square, remove old value
     * insert value in sudoku
     *
     * @param sValue value to display in String format
     * @param idGrid grid id
     * @param idSquare square id on grid (grid id)
     */
    fun insertValueByUser(sValue: String, idGrid: Int, idSquare: Int) {
        val pos = getIndex(idGrid, idSquare)

        val newValue = sValue.toInt()
        val oldValue = sudoku[pos].value

        if (oldValue != 0) {
            updateDigitsAvailable(oldValue, getStartIndexColumn(pos), ::getIndexForColumn, ::add)
            updateDigitsAvailable(oldValue, getStartIndexRow(pos), ::getIndexForRow, ::add)
            updateDigitsAvailable(oldValue, getStartIndexGrid(pos), ::getIndexForGrid, ::add)
            sudoku[pos].value = 0
            digitsToFind++
        }

        insertValue(newValue, R.color.colorValue, pos)

        state.postValue(
            SudokuState.FillSquare(
                sudoku,
                R.string.insert_value,
                newValue
            )
        )
    }

    fun startAlgo() {
        if (digitsToFind == 0) {
            state.postValue(SudokuState.DisplayMessage(R.string.success_sudoku))
        } else if (!searchValue()) {
            state.postValue(SudokuState.DisplayMessage(R.string.error_sudoku))
        }
    }

    /**
     * search first value possible to insert in sudoku
     *
     * @return true if a value is found
     */
    private fun searchValue(): Boolean {
        if (checkOneValueBySquare()) {
            return true
        }
        if (checkOneValue9Time(
                ::getStartIndexGridBy9,
                ::getIndexForGrid,
                ::findIndexByGrid,
                R.string.one_value_by_grid
            )
        ) {
            return true
        }
        if (checkOneValue9Time(
                ::getStartIndexRowBy9,
                ::getIndexForRow,
                ::findIndexByRow,
                R.string.one_value_by_row
            )
        ) {
            return true
        }
        if (checkOneValue9Time(
                ::getStartIndexColumnBy9,
                ::getIndexForColumn,
                ::findIndexByColumn,
                R.string.one_value_by_column
            )
        ) {
            return true
        }
        if (checkPair()) {
            return true
        }
        return false
    }

    /**
     * insert value in sudoku
     * and update other squares in the same row, column and grid
     *
     * @param value value to put in the square
     * @param idTextColor id ressource for textColor
     * @param index square index who will get the value
     */
    private fun insertValue(value: Int, idTextColor: Int, index: Int) {
        sudoku[index].apply {
            this.value = value
            this.idTextColor = idTextColor
        }
        digitsToFind--
        updateDigitsAvailable(value, getStartIndexGrid(index), ::getIndexForGrid, ::remove)
        updateDigitsAvailable(value, getStartIndexRow(index), ::getIndexForRow, ::remove)
        updateDigitsAvailable(value, getStartIndexColumn(index), ::getIndexForColumn, ::remove)
    }

    /**
     * update digits available in square by grid || row || column
     *
     * @param value in [1,9]
     * @param startIndex getStartIndexGrid || getStartIndexRow || getStartIndexColumn
     * @param getIndex getIndexForGrid || getIndexForRow || getIndexForColumn
     * @param action add || remove
     */
    private fun updateDigitsAvailable(
        value: Int,
        startIndex: Int,
        getIndex: (start: Int, index: Int) -> Int,
        action: (squareData: SquareData, value: Int) -> Unit
    ) {
        for (i in 0 until 9) {
            val index = getIndex(startIndex, i)
            if (sudoku[index].value != value) {
                action(sudoku[index], value)
            }
        }
    }

    /**
     * remove the digit from the list of posible digits in the selected square
     *
     * @param squareData selected square
     * @param value to remove
     */
    private fun remove(squareData: SquareData, value: Int) {
        squareData.possibility.remove(value)
    }

    /**
     * add the digit in the list of posible digits in the selected square
     *
     * @param squareData selected square
     * @param value to add
     */
    private fun add(squareData: SquareData, value: Int) {
        squareData.possibility.add(value)
    }

    /**
     * find fist square with a value only available in the set
     * if square is found, update view
     *
     * @return true if square is found else false
     */
    private fun checkOneValueBySquare(): Boolean {
        var result = false
        for (i in sudoku.indices) {
            if (sudoku[i].value == 0 && sudoku[i].possibility.size == 1) {
                val value = sudoku[i].possibility.toList()[0]
                insertValue(value, R.color.colorValueFound, i)

                state.postValue(
                    SudokuState.FillSquareAlgo(
                        sudoku,
                        R.string.one_value_by_square,
                        value,
                        getIndexGrid(i),
                        getIndexSquareInGrid(i)
                    )
                )
                return true
            }
        }
        return result
    }

    /**
     * find fist (row || column || grid) with a digit only available in one square from (row || column || grid)
     * if square is found, update view
     *
     * @param idRes id String to explain result
     *
     * @return true if square is found else false
     */
    private fun checkOneValue9Time(
        getIndexBy9: (i: Int) -> Int,
        getIndex: (start: Int, index: Int) -> Int,
        findIndex: (start: Int, value: Int) -> Int,
        idRes: Int
    ): Boolean {
        for (i in 0 until 9) {
            if (checkOneValue(getIndexBy9(i), getIndex, findIndex, idRes)) {
                return true
            }
        }
        return false
    }

    /**
     * find fist square with a value only available in the row || column || grid
     * if square is found, update view
     *
     * @param idRes id String to explain result
     *
     * @return true if square is found else false
     */
    private fun checkOneValue(
        startIndex: Int,
        getIndex: (start: Int, index: Int) -> Int,
        findIndex: (start: Int, value: Int) -> Int,
        idRes: Int
    ): Boolean {
        val tabCompteur = IntArray(9) { 0 }

        for (i in 0 until 9) {
            val index = getIndex(startIndex, i)
            if (sudoku[index].value == 0) {
                sudoku[index].possibility.forEach {
                    tabCompteur[it - 1] = tabCompteur[it - 1] + 1
                }
            }
        }

        for (i in tabCompteur.indices) {
            if (tabCompteur[i] == 1) {
                val value = i + 1
                val index = findIndex(startIndex, value)
                insertValue(value, R.color.colorValueFound, index)

                state.postValue(
                    SudokuState.FillSquareAlgo(
                        sudoku, idRes, value, getIndexGrid(index), getIndexSquareInGrid(index)
                    )
                )
                return true
            }
        }

        return false
    }

    /**
     * return index in row if value is found
     *
     * @param startIndex will be this number : 0, 8, 18, 27, ..., 72
     * @param value to insert in square
     */
    private fun findIndexByRow(startIndex: Int, value: Int): Int {
        for (i in 0 until 9) {
            var index = startIndex + i
            val squareData = sudoku[index]
            if (squareData.value == 0 && squareData.possibility.contains(value)) {
                return index
            }
        }
        return -1
    }

    /**
     * return index in column if value is found
     *
     * @param startIndex will be this number : 0, 1, 2, 3, ..., 8
     * @param value to insert in square
     */
    private fun findIndexByColumn(startIndex: Int, value: Int): Int {
        for (i in 0 until 9) {
            var index = startIndex + i * 9
            val squareData = sudoku[index]
            if (squareData.value == 0 && squareData.possibility.contains(value)) {
                return index
            }
        }
        return -1
    }

    /**
     * return index in column if value is found
     *
     * @param startIndex will be this number : 0, 3, 6, 27, ..., 60
     * @param value to insert in square
     */
    private fun findIndexByGrid(startIndex: Int, value: Int): Int {
        for (i in 0 until 9) {
            var index = startIndex + (i % 3) + ((i / 3) * 9)
            val squareData = sudoku[index]
            if (squareData.value == 0 && squareData.possibility.contains(value)) {
                return index
            }
        }
        return -1
    }

    private fun checkPair(): Boolean {
        for (i in sudoku.indices) {
            val square = sudoku[i]
            if (square.possibility.size == 2) {
                if(checkPairByGrid(i)){
                    return true
                }
                if(checkPairBy(i, getStartIndexRow(i), ::getIndexForRow)) {
                    return true
                }
                if(checkPairBy(i, getStartIndexColumn(i), ::getIndexForColumn)) {
                    return true
                }
            }
        }
        return false
    }

    private fun checkPairByGrid(indexPair: Int): Boolean {
        val startIndexGrid = getStartIndexGrid(indexPair)
        var setIndex = mutableSetOf<Int>()

        setIndex.addAll(getSquaresContainsPair(startIndexGrid, indexPair, ::getIndexForGrid, ::containsOnlyPair))

        if (setIndex.isEmpty()) {
            setIndex.addAll(getSquaresContainsPair(startIndexGrid, indexPair, ::getIndexForGrid, ::containsPair))
        }

        if (setIndex.size == 1) {
            val indexOtherPair = setIndex.toList()[0]

            updateDigitsAvailableForPair(
                startIndexGrid,
                indexPair,
                indexOtherPair,
                ::getIndexForGrid
            )

            val startIndexColumn = getStartIndexColumn(indexPair)
            if (startIndexColumn == getStartIndexColumn(indexOtherPair)) {
                updateDigitsAvailableForPair(
                    startIndexColumn,
                    indexPair,
                    indexOtherPair,
                    ::getIndexForColumn
                )
            }

            val startIndexRow = getStartIndexRow(indexPair)
            if (startIndexColumn == getStartIndexRow(indexOtherPair)) {
                updateDigitsAvailableForPair(
                    startIndexRow,
                    indexPair,
                    indexOtherPair,
                    ::getIndexForRow
                )
            }

            postValuePair(indexPair, indexOtherPair)

            return true
        }
        return false
    }

    private fun checkPairBy(indexPair: Int, startIndex : Int, getIndexFor: (start: Int, index: Int) -> Int) : Boolean {
        var setIndex = mutableSetOf<Int>()

        setIndex.addAll(getSquaresContainsPair(startIndex, indexPair, getIndexFor, ::containsOnlyPair))

        if (setIndex.size == 1) {
            val indexOtherPair = setIndex.toList()[0]

            updateDigitsAvailableForPair(
                startIndex,
                indexPair,
                indexOtherPair,
                getIndexFor
            )

            postValuePair(indexPair, indexOtherPair)

        }

        return false
    }

    private fun postValuePair(indexPair : Int, indexOtherPair : Int){
        val listSquareSelected = ArrayList<Pair<Int, Int>>()
        listSquareSelected.add(Pair(getIndexGrid(indexPair), getIndexSquareInGrid(indexPair)))
        listSquareSelected.add(
            Pair(
                getIndexGrid(indexOtherPair),
                getIndexSquareInGrid(indexOtherPair)
            )
        )

        state.postValue(
            SudokuState.PairAlgo(
                sudoku,
                R.string.pair_found_grid,
                listSquareSelected,
                sudoku[indexPair].possibility
            )
        )
    }

    private fun updateDigitsAvailableForPair(startIndex: Int, indexPair: Int, indexOtherPair: Int,
                                             getIndexFor: (start: Int, index: Int) -> Int) {
        for (i in 0 until 9) {
            val index = getIndexFor(startIndex, i)
            if (index == indexOtherPair) {
                removeValueNotPair(index, indexPair)
            } else if (indexPair != index) {
                removeValuePair(index, indexPair)
            }
        }
    }

    private fun getSquaresContainsPair(
        startIndex: Int,
        indexPair: Int,
        getIndexFor: (start: Int, index: Int) -> Int,
        contains: (Set<Int>, Set<Int>) -> Boolean
    ):
            MutableSet<Int> {
        var setIndex = mutableSetOf<Int>()
        for (i in 0 until 9) {
            val index = getIndexFor(startIndex, i)
            if (indexPair != index) {
                if (contains(sudoku[index].possibility, sudoku[indexPair].possibility)) {
                    setIndex.add(index)
                }
            }
        }
        return setIndex
    }

    private fun containsOnlyPair(setPossibility: Set<Int>, setPossibilityPair: Set<Int>) =
        setPossibility.size == 2 && setPossibility.containsAll(setPossibilityPair)

    private fun containsPair(setPossibility: Set<Int>, setPossibilityPair: Set<Int>) =
        setPossibility.containsAll(setPossibilityPair)

    private fun removeValueNotPair(index: Int, indexPair: Int) {
        val setPossibility = sudoku[index].possibility
        val setPossibilityPair = sudoku[indexPair].possibility
        val listValue: List<Int> = setPossibility.toList()
        listValue.forEach {
            if (!setPossibilityPair.contains(it)) {
                setPossibility.remove(it)
            }
        }

    }

    private fun removeValuePair(index: Int, indexPair: Int) {
        sudoku[index].possibility.removeAll(sudoku[indexPair].possibility)
    }

    /**
     * compute the first square index of column with index
     *
     * @param index in [0,8]
     * @return value in [0,8]
     */
    private fun getStartIndexColumnBy9(index: Int): Int = index

    /**
     * compute the first square index of grid with index
     *
     * @param index in [0,8]
     * @return one value in this set {0, 3, 6, 27, 30, 33, 54, 57, 60}
     */
    private fun getStartIndexGridBy9(index: Int): Int = (3 * index) + (9 * 2 * (index / 3))

    /**
     * compute the first square index of row with index
     *
     * @param index in [0,8]
     * @return one value in this set {0, 9, 18, 36, 45, 54, 63, 72}
     */
    private fun getStartIndexRowBy9(index: Int): Int = index * 9

    /**
     * compute the square index of row with startIndex & index
     *
     * @param startIndex in {0, 9, 18, 36, 45, 54, 63, 72}
     * @param index in [0,8]
     */
    private fun getIndexForRow(startIndex: Int, index: Int): Int = startIndex + index

    /**
     * compute the square index of column with startIndex & index
     *
     * @param startIndex in [0,8]
     * @param index in [0,8]
     */
    private fun getIndexForColumn(startIndex: Int, index: Int): Int = startIndex + index * 9

    /**
     * compute the square index of grid with startIndex & index
     *
     * @param startIndex in {0, 3, 6, 27, 30, 33, 54, 57, 60}
     * @param index in [0,8]
     */
    private fun getIndexForGrid(startIndex: Int, index: Int): Int =
        startIndex + (index % 3) + ((index / 3) * 9)

    /**
     * compute first square index in row
     * * if index is 70, return 63
     * @param index in  sudoku
     */
    private fun getStartIndexRow(index: Int) = (index / 9) * 9

    /**
     * compute first square index in column
     * * if index is 70, return 7
     * @param index in  sudoku
     */
    private fun getStartIndexColumn(index: Int) = index % 9

    /**
     * compute first square index in grid
     * * if index is 70, return 60
     * @param index in  sudoku
     */
    private fun getStartIndexGrid(index: Int): Int {
        val column = index % 9
        val row = index / 9
        return (column / 3) * 3 + ((row / 3) * 3) * 9
    }

    /**
     * convert grid index and square index to get the index in Sudoku
     * @param idGrid id of selected grid
     * @param idSquare id of selected square in the grid
     */
    private fun getIndex(idGrid: Int, idSquare: Int) =
        (3 * (idGrid % 3) + (idSquare % 3)) + (((idSquare / 3) + (idGrid / 3) * 3) * 9)

    /**
     * compute grid index with index
     * * if index is 70, return 8
     * @param index in  sudoku
     */
    private fun getIndexGrid(index: Int): Int {
        val indexGrid = getStartIndexGrid(index)

        return ((indexGrid % 9) / 3) + (indexGrid / 9)
    }

    /**
     * compute square index in grid with index
     * if index is 70, return 4
     * @param index in  sudoku
     */
    private fun getIndexSquareInGrid(index: Int): Int {
        val indexRow = getStartIndexRow(index)
        val indexColumn = getStartIndexColumn(index)

        return (((indexRow / 9) % 3) * 3) + (indexColumn % 3)
    }
}
