package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.GridLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.data.SquareData

class GridView : GridLayout {

    private var onGridListener: OnGridListener? = null

    private var squareViewSelected: SqareView? = null
    private val listSquareView: MutableList<SqareView> = mutableListOf()

    private val onClickListener = OnClickListener { v ->
        when (v) {
            is SqareView -> {
                squareViewSelected?.unSelectSquare()
                squareViewSelected = v
                squareViewSelected?.selectSquare()
                onGridListener?.onClickSquare((v.tag as String).toInt())
            }
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_grid, this, true)
        columnCount = 3

        forEach {
            listSquareView.add(it as SqareView)
            it.setOnClickListener(onClickListener)
        }
    }

    fun setOnGridListener(listener: OnGridListener) {
        onGridListener = listener
    }

    fun updateGrid(square: Int, solution: SquareData) {
        listSquareView[square].updateSquare(solution)
        squareViewSelected?.unSelectSquare()
        squareViewSelected = null
    }

    fun unSelectedSquare() {
        squareViewSelected?.unSelectSquare()
        squareViewSelected = null
    }

    fun selectSquare(idSquare: Int, value: Int) {
        squareViewSelected = listSquareView[idSquare]
        squareViewSelected?.selectSquare(value)
    }

    interface OnGridListener {
        fun onClickSquare(position: Int)
    }

}