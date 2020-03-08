package com.lconsulting.sudoku.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.ui.view.SudokuView
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {

        @IntDef(PLAY, STOP)
        @Retention(AnnotationRetention.SOURCE)
        annotation class StateResolver

        const val PLAY = 0
        const val STOP = 1

        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: SudokuViewModel

    @StateResolver
    private var statePlayer = PLAY

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
                R.id.btnPlay -> {
                    if(statePlayer == STOP){
                        btnPlay.setImageDrawable(resources.getDrawable(R.drawable.baseline_play_arrow_black_24))
                        statePlayer = PLAY
                        isRepeat = false
                    }else {
                        viewModel.startAlgo()
                    }
                }
                R.id.btnRepeat -> {
                    statePlayer = STOP
                    btnPlay.setImageDrawable(resources.getDrawable(R.drawable.baseline_stop_black_24))
                    isRepeat = true
                    viewModel.startAlgo()
                }
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
        disableActionButton()
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
                }, 500)
            }
            is SudokuState.Reset ->{
                sudoku.updateSudoku(state.solution)
                tvState.text = resources.getString(R.string.insert_a_value)
            }
            is SudokuState.InsertValue -> {
                tvState.text = resources.getString(R.string.insert_a_value)
            }
            is SudokuState.DisplayMessage -> {
                tvState.text = resources.getString(state.idResString)
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

    private fun disableActionButton() {
        btnPrevious.isEnabled  = false
        btnNext.isEnabled = false
    }

    private fun updateDigitsButton(possibility: MutableSet<Int>) {
        llButton.forEach {
            val tv = it as TextView
            tv.isEnabled = possibility.contains(tv.text.toString().toInt())
        }
    }

}
