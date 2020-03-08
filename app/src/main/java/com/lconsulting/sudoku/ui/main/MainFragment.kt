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

    private lateinit var viewModel: SudokuViewModel

    private var idGrid: Int = 0
    private var idSquare: Int = 0
    private var isRepeat : Boolean = false

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            is TextView -> {
                viewModel.insertValueByUser(v.text.toString(), idGrid, idSquare)
                disableDigitsButton()
            }
            else -> when (v.id) {
                R.id.bPlay -> viewModel.startAlgo()
                R.id.bRepeat -> isRepeat = !isRepeat

            }
        }
    }

    private val onSudokuListener = object : SudokuView.OnSudokuListener {
        override fun onClickSquare(idGrid: Int, idSquare: Int) {
            this@MainFragment.idGrid = idGrid
            this@MainFragment.idSquare = idSquare
            viewModel.getDigitAvailable(idGrid, idSquare)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SudokuViewModel::class.java)
        viewModel.state.observe(this, Observer { updateUI(it) })
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

        disableDigitsButton()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_reset -> viewModel.reset()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateUI(state: SudokuState) {
        when (state) {
            is SudokuState.FillSquare -> {
                sudoku.updateSudoku(state.sudoku)
                tvState.text = resources.getString(state.idRes, state.value)
            }
            is SudokuState.FillSquareAlgo -> {
                tvState.text = resources.getString(state.idRes, state.value)
                sudoku.selectSquare(state.idGrid ,state.idSquare, state.value)
                Handler().postDelayed({
                    sudoku.updateSudoku(state.sudoku)
                    if (isRepeat){
                        viewModel.startAlgo()
                    }
                }, 2500)
            }
            is SudokuState.Reset ->{
                sudoku.updateSudoku(state.solution)
                tvState.text = resources.getString(R.string.insert_a_value)
            }
            is SudokuState.InsertValue -> {
                tvState.text = resources.getString(R.string.insert_a_value)
            }
            is SudokuState.Error -> {
                tvState.text = resources.getString(R.string.error_sudoku)
            }
            is SudokuState.DisplayButton -> updateDigitsButton(state.possibility)
        }
    }

    private fun disableDigitsButton(){
        llButton.forEach {
            val tv = it as TextView
            tv.isEnabled = false
        }
    }

    private fun updateDigitsButton(possibility: MutableSet<Int>) {
        llButton.forEach {
            val tv = it as TextView
            tv.isEnabled = possibility.contains(tv.text.toString().toInt())
        }
    }

}
