package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R
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

    fun setValue(solution : MutableSet<Int>) {
        if(solution.size == 1){
            tvValue.text = solution.toList()[0].toString()
            tvValue.visibility = View.VISIBLE
            grid.visibility = View.INVISIBLE
        }else{
            tvValue.visibility = View.INVISIBLE
            grid.visibility = View.VISIBLE
            listSquareView.forEach {
                if(solution.contains(it.text.toString().toInt())){
                    it.visibility = View.VISIBLE
                }else{
                    it.visibility = View.INVISIBLE
                }
            }
        }
    }

}