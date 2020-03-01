package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.lconsulting.sudoku.R
import kotlinx.android.synthetic.main.view_square.view.*

class SqareView : ConstraintLayout {



    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_square, this, true)
    }

    fun setValue(value: Int) {
        tvValue.text = value.toString()
        tvValue.visibility = View.VISIBLE
        grid.visibility = View.INVISIBLE
    }


}