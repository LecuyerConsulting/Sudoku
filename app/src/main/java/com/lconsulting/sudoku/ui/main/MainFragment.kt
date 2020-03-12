package com.lconsulting.sudoku.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.data.SquareData
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
    private var isRepeat: Boolean = false

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            is Button -> {
//                sudoku.unEnlightenedValue()
//                sudoku.enlightenedValue(v.text.toString().toInt())
                viewModel.insertValueByUser(v.text.toString(), idGrid, idSquare)
                disableDigitsButton()
            }
            else -> when (v.id) {
                R.id.main ->{
//                    sudoku.unEnlightenedValue()
//                    sudoku.unSelectSquare()
                }
                R.id.btnPlay -> {
                    if (statePlayer == STOP) {
                        setRepeatMode(PLAY, R.drawable.baseline_play_arrow_black_24, false)
                    } else {
                        viewModel.startAlgo()
                    }
                }
                R.id.btnRepeat -> {
                    setRepeatMode(STOP, R.drawable.baseline_stop_black_24, true)
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

        main.setOnClickListener(onClickListener)

        disableDigitsButton()
        hideActionButton()
    }

    override fun onStart() {
        super.onStart()
        viewModel.insertValueByUser("9",0, 0)
        viewModel.insertValueByUser("0",0, 1)
        viewModel.insertValueByUser("0",0, 2)
        viewModel.insertValueByUser("0",0, 3)
        viewModel.insertValueByUser("2",0, 4)
        viewModel.insertValueByUser("0",0, 5)
        viewModel.insertValueByUser("0",0, 6)
        viewModel.insertValueByUser("0",0, 7)
        viewModel.insertValueByUser("6",0, 8)

        viewModel.insertValueByUser("0",1, 0)
        viewModel.insertValueByUser("5",1, 1)
        viewModel.insertValueByUser("0",1, 2)
        viewModel.insertValueByUser("3",1, 3)
        viewModel.insertValueByUser("0",1, 4)
        viewModel.insertValueByUser("6",1, 5)
        viewModel.insertValueByUser("1",1, 6)
        viewModel.insertValueByUser("0",1, 7)
        viewModel.insertValueByUser("0",1, 8)

        viewModel.insertValueByUser("0",2, 0)
        viewModel.insertValueByUser("8",2, 1)
        viewModel.insertValueByUser("0",2, 2)
        viewModel.insertValueByUser("0",2, 3)
        viewModel.insertValueByUser("0",2, 4)
        viewModel.insertValueByUser("0",2, 5)
        viewModel.insertValueByUser("0",2, 6)
        viewModel.insertValueByUser("0",2, 7)
        viewModel.insertValueByUser("0",2, 8)

        viewModel.insertValueByUser("3",3, 0)
        viewModel.insertValueByUser("0",3, 1)
        viewModel.insertValueByUser("0",3, 2)
        viewModel.insertValueByUser("0",3, 3)
        viewModel.insertValueByUser("9",3, 4)
        viewModel.insertValueByUser("0",3, 5)
        viewModel.insertValueByUser("0",3, 6)
        viewModel.insertValueByUser("0",3, 7)
        viewModel.insertValueByUser("2",3, 8)

        viewModel.insertValueByUser("0",4, 0)
        viewModel.insertValueByUser("0",4, 1)
        viewModel.insertValueByUser("0",4, 2)
        viewModel.insertValueByUser("0",4, 3)
        viewModel.insertValueByUser("3",4, 4)
        viewModel.insertValueByUser("0",4, 5)
        viewModel.insertValueByUser("4",4, 6)
        viewModel.insertValueByUser("0",4, 7)
        viewModel.insertValueByUser("0",4, 8)

        viewModel.insertValueByUser("0",5, 0)
        viewModel.insertValueByUser("1",5, 1)
        viewModel.insertValueByUser("2",5, 2)
        viewModel.insertValueByUser("5",5, 3)
        viewModel.insertValueByUser("0",5, 4)
        viewModel.insertValueByUser("0",5, 5)
        viewModel.insertValueByUser("0",5, 6)
        viewModel.insertValueByUser("0",5, 7)
        viewModel.insertValueByUser("0",5, 8)

        viewModel.insertValueByUser("0",6, 0)
        viewModel.insertValueByUser("8",6, 1)
        viewModel.insertValueByUser("0",6, 2)
        viewModel.insertValueByUser("0",6, 3)
        viewModel.insertValueByUser("0",6, 4)
        viewModel.insertValueByUser("7",6, 5)
        viewModel.insertValueByUser("0",6, 6)
        viewModel.insertValueByUser("0",6, 7)
        viewModel.insertValueByUser("0",6, 8)

        viewModel.insertValueByUser("0",7, 0)
        viewModel.insertValueByUser("0",7, 1)
        viewModel.insertValueByUser("0",7, 2)
        viewModel.insertValueByUser("5",7, 3)
        viewModel.insertValueByUser("0",7, 4)
        viewModel.insertValueByUser("8",7, 5)
        viewModel.insertValueByUser("0",7, 6)
        viewModel.insertValueByUser("0",7, 7)
        viewModel.insertValueByUser("4",7, 8)

        viewModel.insertValueByUser("4",8, 0)
        viewModel.insertValueByUser("0",8, 1)
        viewModel.insertValueByUser("1",8, 2)
        viewModel.insertValueByUser("0",8, 3)
        viewModel.insertValueByUser("0",8, 4)
        viewModel.insertValueByUser("0",8, 5)
        viewModel.insertValueByUser("0",8, 6)
        viewModel.insertValueByUser("9",8, 7)
        viewModel.insertValueByUser("0",8, 8)

//        isRepeat = true
//        viewModel.startAlgo()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
                sudoku.selectSquare(state.idGrid, state.idSquare, state.value)
                handler(state.sudoku)
            }
            is SudokuState.Reset -> {
                sudoku.updateSudoku(state.solution)
                tvState.text = resources.getString(R.string.insert_a_value)
            }
            is SudokuState.DisplayMessage -> {
                setRepeatMode(PLAY, R.drawable.baseline_play_arrow_black_24, false)
                tvState.text = resources.getString(state.idResString)
            }
            is SudokuState.DisplayButton -> updateDigitsButton(state.possibility)
            is SudokuState.PairAlgo -> {
                sudoku.selectSquares(state.listSquareSelected, state.listValueSelected)
                val listValues = state.listValueSelected.toList()
                tvState.text = resources.getString(state.idRes, listValues[0], listValues[1])
                handler(state.sudoku)
            }
            is SudokuState.IntersectionAlgo -> {
                sudoku.selectSquares(state.listSquareSelected, state.value)
                tvState.text = resources.getString(state.idRes, state.value)
                handler(state.sudoku)
            }
        }
    }

    private fun handler(sudokayArray: Array<SquareData>) {
        Handler().postDelayed({
            sudoku.updateSudoku(sudokayArray)
            if (isRepeat) {
                viewModel.startAlgo()
            }
        }, 100)
    }

    private fun setRepeatMode(@StateResolver state: Int, idResDrawable: Int, isRepeat: Boolean) {
        statePlayer = state
        btnPlay.setImageDrawable(resources.getDrawable(idResDrawable))
        this.isRepeat = isRepeat
    }

    private fun disableDigitsButton() {
        llButton.forEach {
            val tv = it as TextView
            tv.isEnabled = false
        }
    }

    private fun hideActionButton() {
        btnPrevious.visibility = View.INVISIBLE
        btnNext.visibility = View.INVISIBLE
    }

    private fun updateDigitsButton(possibility: MutableSet<Int>) {
        llButton.forEach {
            val tv = it as TextView
            tv.isEnabled = possibility.contains(tv.text.toString().toInt())
        }
    }

}
