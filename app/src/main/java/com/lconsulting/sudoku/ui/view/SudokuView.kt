package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.GridLayout
import androidx.core.view.forEach
import com.lconsulting.sudoku.R

class SudokuView : GridLayout {

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
            (it as GridView).setOnGridListener(object : GridView.OnGridListener{
                override fun onClickSquare(position: Int) {
                    Log.d("tom971", "click grille ${it.tag} cellule $position")
                }
            })
        }

    }

}