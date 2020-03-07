package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.ui.data.SquareData

class GridView : GridLayout {

    private var onGridListener: OnGridListener? = null

    private var squareViewSelected: SqareView? = null
    private val listSquareView: MutableList<SqareView> = mutableListOf()

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            is SqareView -> {
                squareViewSelected?.background = resources.getDrawable(R.drawable.background_square)
                squareViewSelected = v
                squareViewSelected?.background = resources.getDrawable(R.drawable.background_square_selected)
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

    fun setValue(square: Int, solution : SquareData) {
        listSquareView[square].setValue(solution)
    }

    fun unSelectedSquare() {
        squareViewSelected?.background = resources.getDrawable(R.drawable.background_square)
    }

    interface OnGridListener {
        fun onClickSquare(position: Int)
    }

}