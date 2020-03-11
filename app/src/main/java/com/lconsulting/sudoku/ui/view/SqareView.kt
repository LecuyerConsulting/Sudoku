package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.data.SquareData
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

    fun updateSquare(square: SquareData) {
        if (square.value != 0) {
            tvValue.apply {
                text = square.value.toString()
                visibility = View.VISIBLE
                setTextColor(
                    resources.getColor(square.idTextColor)
                )
            }
            setTextViewUI(listSquareView[square.value - 1], R.color.colorText, Typeface.NORMAL)
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
            }
        }
    }

    fun selectSquare(value: Int) {
        setTextViewUI(listSquareView[value - 1], R.color.colorValueFound, Typeface.BOLD)
        selectSquare()
    }

    fun unSelectSquare() {
        listSquareView.forEach {
            setTextViewUI(it, R.color.colorText, Typeface.NORMAL)
        }
        background = resources.getDrawable(R.drawable.background_square)
    }

    fun selectSquare() {
        background = resources.getDrawable(R.drawable.background_square_selected)
    }

    private fun setTextViewUI(tv : TextView, idResColor : Int, idTypeface: Int){
        tv.apply {
            setTextColor(resources.getColor(idResColor))
            setTypeface(null, idTypeface)
        }
    }

    fun selectSquare(listValueSelected: Set<Int>) {
        listValueSelected.toList().forEach {
            setTextViewUI(listSquareView[it-1], R.color.colorValueFound, Typeface.BOLD)
        }
        selectSquare()
    }

}
