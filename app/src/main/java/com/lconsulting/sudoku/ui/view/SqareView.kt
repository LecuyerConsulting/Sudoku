package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.ui.data.SquareData
import kotlinx.android.synthetic.main.view_square.view.*

class SqareView : ConstraintLayout {

    private val listSquareView: MutableList<TextView> = mutableListOf()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_square, this, true)
        grid.forEach {
            listSquareView.add(it as TextView)
        }
    }

    fun setValue(square: SquareData) {
        if (square.value != 0) {
            tvValue.text = square.value.toString()
            tvValue.visibility = View.VISIBLE
            tvValue.setTextColor(
                resources.getColor(square.textColor)
            )
            grid.visibility = View.INVISIBLE
        } else {
            tvValue.visibility = View.INVISIBLE
            grid.visibility = View.VISIBLE
            listSquareView.forEach {
                if (square.possibility.contains(it.text.toString().toInt())) {
                    it.visibility = View.VISIBLE
                } else {
                    it.visibility = View.INVISIBLE
                }
                it.setTextColor(
                    resources.getColor(R.color.colorText)
                )
            }
        }
    }

    fun selectValue(value: Int) {
        listSquareView[value - 1].setTextColor(
            resources.getColor(R.color.colorValueFound)
        )
    }

}