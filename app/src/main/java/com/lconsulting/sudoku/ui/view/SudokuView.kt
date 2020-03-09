package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.GridLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.data.SquareData

class SudokuView : GridLayout {

    private var onSudokuListener: OnSudokuListener? = null
    private var gridViewSelected: GridView? = null
    private val listGridView: MutableList<GridView> = mutableListOf()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_sudoku, this, true)
        columnCount = 3

        forEach {
            listGridView.add(it as GridView)
            (it as GridView).setOnGridListener(object : GridView.OnGridListener {
                override fun onClickSquare(position: Int) {
                    if (gridViewSelected != it) {
                        gridViewSelected?.unSelectedSquare()
                    }
                    gridViewSelected = it
                    onSudokuListener?.onClickSquare(it.tag.toString().toInt(), position)
                }
            })
        }
    }

    fun setOnSudokuListener(listener: OnSudokuListener) {
        onSudokuListener = listener
    }

    fun updateSudoku(solution: Array<SquareData>) {
        for (i in solution.indices) {
            val posLine = i / 9
            val posColumn = i % 9
            val square = (posColumn % 3) + (posLine % 3) * 3
            val grid = (posColumn / 3) + (posLine / 3) * 3

            listGridView[grid].updateGrid(square, solution[i])
        }
    }

    fun selectSquare(idGrid: Int, idSquare: Int, value : Int) {
        gridViewSelected = listGridView[idGrid]
        gridViewSelected?.selectSquare(idSquare, value)

    }

    interface OnSudokuListener {
        fun onClickSquare(idGrid: Int, idSquare: Int)
    }

}