package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R

class GridView : ConstraintLayout {

    private val listSquareView: MutableList<SquareView> = mutableListOf()

    var listener: GridViewListener? = null

    var possibility: MutableSet<Int>? = null

    fun clear() {
        for (squareView in listSquareView) {
            squareView.displaySquareEmpty()
        }
    }

    private val onClickListener = OnClickListener {
        val valueSelected = it.tag.toString().toInt()
        if (possibility!!.contains(valueSelected)) {
            Log.d("tom971", "GridView onClickListener ${it.tag}")
            listener?.onClickValue(valueSelected)
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
        forEach {
            when (it) {
                is SquareView -> {
                    listSquareView.add(it)
                    it.setOnClickListener(onClickListener)
                }
            }
        }
    }

    fun refreshView(possibility: MutableSet<Int>) {
        this@GridView.possibility = possibility
        listSquareView.forEach { squareView ->
            val valueString = squareView.tag.toString()
            val valueInt = valueString.toInt()
            squareView.displayValue(
                valueString,
                possibility.contains(valueInt)
            )
        }
    }

    interface GridViewListener {
        fun onClickValue(value: Int)
    }
}