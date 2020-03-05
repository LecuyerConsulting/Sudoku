package com.lconsulting.sudoku.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            is TextView -> {
                viewModel.checkValue(v.text.toString(), line, position)

            }
        }
    }

    private val onSudokuListener = object : SudokuView.OnSudokuListener {
        override fun onClickSquare(g: Int, p: Int) {
            line = g
            position = p
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.state.observe(this, Observer { updateUI(it) })
        viewModel.resetSudoku()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        llButton.forEach {
            it.setOnClickListener(onClickListener)
        }

        sudoku.setOnSudokuListener(onSudokuListener)
    }

    private fun updateUI(state: SudokuState) {
        when (state) {
            is SudokuState.SuccesSudokuState -> sudoku.setValue(state.solution)
            is SudokuState.ErrorSudokuState -> Toast.makeText(
                requireContext(),
                "valeur impossible",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
