package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lconsulting.sudoku.R
import kotlinx.android.synthetic.main.view_grid.view.*

class GridView : ConstraintLayout{

    private val listSquareView: MutableList<SquareView> = mutableListOf()

    fun clear() {
        for (squareView in listSquareView) {
            squareView.displaySquareEmpty()
        }
    }

    fun displayValues() {
        listSquareView.forEachIndexed { index, squareView ->
            squareView.displayValue(index.toString())
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        LayoutInflater.from(context).inflate(R.layout.view_grid, this, true)
        listSquareView.add(gsquare1)
        listSquareView.add(gsquare2)
        listSquareView.add(gsquare3)
        listSquareView.add(gsquare4)
        listSquareView.add(gsquare5)
        listSquareView.add(gsquare6)
        listSquareView.add(gsquare7)
        listSquareView.add(gsquare8)
        listSquareView.add(gsquare9)

    }
}