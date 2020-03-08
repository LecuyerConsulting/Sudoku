package com.lconsulting.sudoku.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.TextView
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.ui.view.SudokuView
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var line: Int = 0
    private var position: Int = 0
    private var isRepeat : Boolean = false

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            is TextView -> {
                viewModel.checkValue(v.text.toString(), line, position)
            }
            else -> when (v.id) {
                R.id.bPlay -> viewModel.startAlgo()
                R.id.bRepeat -> isRepeat = !isRepeat

            }
        }
    }

    private val onSudokuListener = object : SudokuView.OnSudokuListener {
        override fun onClickSquare(g: Int, p: Int) {
            line = g
            position = p
            viewModel.getNumberAvailable(g, p)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.state.observe(this, Observer { updateUI(it) })
        viewModel.resetSudoku()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        llButton.forEach {
            it.setOnClickListener(onClickListener)
        }
        llAction.forEach {
            it.setOnClickListener(onClickListener)
        }

        sudoku.setOnSudokuListener(onSudokuListener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_reset -> viewModel.resetSudoku()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateUI(state: SudokuState) {
        when (state) {
            is SudokuState.FillSquareSudokuState -> {
                sudoku.setValue(state.solution)
                tvState.text = resources.getString(state.idRes, state.value)
            }
            is SudokuState.FillSquareAlgoSudokuState -> {
                tvState.text = resources.getString(state.idRes, state.value)
                sudoku.squareSelected(state.idGrid ,state.idSquare, state.value)
                Handler().postDelayed({
                    sudoku.setValue(state.solution)
                    if (isRepeat){
                        viewModel.startAlgo()
                    }
                }, 500)
            }
            is SudokuState.ResetSudokuState ->{
                sudoku.setValue(state.solution)
                tvState.text = resources.getString(R.string.insert_a_value)
            }
            is SudokuState.InsertValueSudokuState -> {
                tvState.text = resources.getString(R.string.insert_a_value)
            }
            is SudokuState.ErrorSudokuState -> {
                tvState.text = resources.getString(R.string.error_sudoku)
            }
            is SudokuState.DisplayButtonState -> refreshButtonNumber(state.possibility)
        }
    }

    private fun refreshButtonNumber(possibility: MutableSet<Int>) {
        llButton.forEach {
            val tv = it as TextView
            tv.isEnabled = possibility.contains(tv.text.toString().toInt())
        }
    }

}
