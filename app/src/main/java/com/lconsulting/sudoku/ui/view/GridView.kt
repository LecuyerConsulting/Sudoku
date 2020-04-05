package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R

class GridView : ConstraintLayout {

    private val listSquareView: MutableList<SquareView> = mutableListOf()

    var listener : GridViewListener? = null

    fun clear() {
        for (squareView in listSquareView) {
            squareView.displaySquareEmpty()
        }
    }

    fun displayValues() {
        listSquareView.forEach { squareView ->
            squareView.displayValue(squareView.tag.toString())
        }
    }

    private val onClickListener = OnClickListener {
        listener?.onClickValue(it.tag.toString().toInt())
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_grid, this, true)
        forEach {
            when (it) {
                is SquareView -> {
                    listSquareView.add(it)
                    it.setOnClickListener(onClickListener)
                }
            }
        }
    }

    interface GridViewListener{
        fun onClickValue(value : Int)
    }
}