package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.forEach
import com.lconsulting.sudoku.R
import com.lconsulting.sudoku.data.SquareData
import com.lconsulting.sudoku.ui.view.GridView.GridViewListener

class TouchPadView : MotionLayout {

    private val stateSubLevelClose = StateViewData(0.0f, 0.3f)
    private val stateSubLevelOpen = StateViewData(1.0f, 1.0f)

    private val stateOpenSquare = StateViewData(1.0f, 1.0f)
    private val stateCloseSquare = StateViewData(0.0f, 0.0f)

    private val constraintConnectData1 = mutableListOf(
        ConstraintConnectData(ConstraintSet.BOTTOM, R.id.square4, ConstraintSet.BOTTOM),
        ConstraintConnectData(ConstraintSet.RIGHT, R.id.square2, ConstraintSet.RIGHT)
    )

    private val constraintConnectData2 = mutableListOf(
        ConstraintConnectData(ConstraintSet.BOTTOM, R.id.square5, ConstraintSet.BOTTOM),
        ConstraintConnectData(ConstraintSet.RIGHT, R.id.square6, ConstraintSet.RIGHT)
    )

    private val constraintConnectData3 = mutableListOf(
        ConstraintConnectData(ConstraintSet.BOTTOM, R.id.square6, ConstraintSet.BOTTOM),
        ConstraintConnectData(ConstraintSet.LEFT, R.id.square2, ConstraintSet.LEFT)
    )

    private val constraintConnectData4 = mutableListOf(
        ConstraintConnectData(ConstraintSet.BOTTOM, R.id.square7, ConstraintSet.BOTTOM),
        ConstraintConnectData(ConstraintSet.RIGHT, R.id.square5, ConstraintSet.RIGHT)
    )

    private val constraintConnectData5 = mutableListOf(
        ConstraintConnectData(ConstraintSet.BOTTOM, R.id.square8, ConstraintSet.BOTTOM),
        ConstraintConnectData(ConstraintSet.RIGHT, R.id.square6, ConstraintSet.RIGHT)
    )

    private val constraintConnectData6 = mutableListOf(
        ConstraintConnectData(ConstraintSet.BOTTOM, R.id.square9, ConstraintSet.BOTTOM),
        ConstraintConnectData(ConstraintSet.LEFT, R.id.square5, ConstraintSet.LEFT)
    )

    private val constraintConnectData7 = mutableListOf(
        ConstraintConnectData(ConstraintSet.TOP, R.id.square5, ConstraintSet.TOP),
        ConstraintConnectData(ConstraintSet.RIGHT, R.id.square5, ConstraintSet.RIGHT)
    )

    private val constraintConnectData8 = mutableListOf(
        ConstraintConnectData(ConstraintSet.TOP, R.id.square5, ConstraintSet.TOP),
        ConstraintConnectData(ConstraintSet.RIGHT, R.id.square6, ConstraintSet.RIGHT)
    )

    private val constraintConnectData9 = mutableListOf(
        ConstraintConnectData(ConstraintSet.TOP, R.id.square5, ConstraintSet.TOP),
        ConstraintConnectData(ConstraintSet.LEFT, R.id.square5, ConstraintSet.LEFT)
    )

    private val constraintCenter = mutableListOf(
        ConstraintConnectData(ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM),
        ConstraintConnectData(ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT),
        ConstraintConnectData(ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT),
        ConstraintConnectData(ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
    )

    private val listConstraint = mutableListOf(
        constraintConnectData1,
        constraintConnectData2,
        constraintConnectData3,
        constraintConnectData4,
        constraintConnectData5,
        constraintConnectData6,
        constraintConnectData7,
        constraintConnectData8,
        constraintConnectData9
    )

    private val mapSquare: MutableMap<Int, SquareView> = HashMap(9)

    private val listSquareView: MutableList<SquareView> = mutableListOf()
    private val listIdRes = mutableListOf(9)

    private var lastConstraintConnectDataUsed = constraintConnectData5

    private var isOpen = false

    private var squareTransition: MotionScene.Transition? = null

    private lateinit var subLevelView: View

    private var isLevel: Boolean = false

    var touchPadListener: TouchPadListener? = null

    private var idGridSelected = 0

    private var listSquareData: MutableList<SquareData>? = null

    private val onClickListener = OnClickListener {
        when (it) {
            is SquareView -> {
                idGridSelected = it.tag.toString().toInt() - 1
                touchPadListener?.onSelectIdGrid(idGridSelected)
            }
        }
    }

    private val onGridViewListener = object : GridViewListener {
        override fun onClickValue(value: Int) {
            touchPadListener?.onSelectValue(value)
        }
    }

    private val subTouchPadListener = object : TouchPadListener {
        override fun onSelectIdGrid(idGrid: Int) {
            touchPadListener?.onSelectIdSquare(idGrid)
        }

        override fun onSelectIdSquare(idSquare: Int) {
            //nothing to do
        }

        override fun onSelectValue(value: Int) {
            touchPadListener?.onSelectValue(value)
        }

        override fun onUnSelectIdGrid(idGrid: Int) {
            touchPadListener?.onUnSelectIdSquare(idGrid)
        }

        override fun onUnSelectIdSquare(idSquare: Int) {
            //nothing to do
        }

    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_touchpad, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initValues()
        initConstraintSubLevel()
        initScene()
    }

    private fun initValues() {
        forEach {
            when (it) {
                is SquareView -> {
                    mapSquare[it.id] = it
                    listSquareView.add(it)
                    listIdRes.add(it.id)
                    it.setOnClickListener(onClickListener)
                }
                is TouchPadView -> {
                    subLevelView = it
                    it.touchPadListener = subTouchPadListener
                    isLevel = true
                }
                is GridView -> {
                    subLevelView = it
                    isLevel = false
                    it.listener = onGridViewListener
                }
            }
        }
    }

    private fun initConstraintSubLevel() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this);
        constraintSet.connect(
            subLevelView.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            0
        )
        constraintSet.connect(
            subLevelView.id,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            0
        )

        constraintSet.setAlpha(subLevelView.id, stateSubLevelClose.alpha)
        constraintSet.setScaleX(subLevelView.id, stateSubLevelClose.scale)
        constraintSet.setScaleY(subLevelView.id, stateSubLevelClose.scale)
        constraintSet.applyTo(this)
    }

    private fun initScene() {
        squareTransition = getTransition(R.id.transition)

        val startId = squareTransition!!.startConstraintSetId
        val startSet = getConstraintSet(startId)
        startSet.clone(this)

        val endId = squareTransition!!.endConstraintSetId
        val endSet = getConstraintSet(endId)
        endSet.clone(this)

        setTransition(squareTransition!!)
        rebuildScene()
    }

    fun openTouchPad(listSquareData: MutableList<SquareData>) {
        (subLevelView as TouchPadView).refreshView(listSquareData)
        openTouchPad(listConstraint[idGridSelected])
    }

    fun openSubTouchPad(idSquare: Int) {
        (subLevelView as TouchPadView).refreshSubView(idSquare)
    }

    private fun refreshView(listSquareData: MutableList<SquareData>) {
        this@TouchPadView.listSquareData = listSquareData
        listSquareView.forEachIndexed { index, squareView ->
            squareView.displayValue(listSquareData[index])
        }
    }

    private fun refreshSubView(idSquare: Int) {
        (subLevelView as GridView).refreshView(listSquareData!![idSquare].possibility)
        openTouchPad(listConstraint[idGridSelected])
    }

    fun refreshTouchPad(listSquareData: MutableList<SquareData>){
        (subLevelView as TouchPadView).refreshSubTouchPad(listSquareData)
    }

    private fun refreshSubTouchPad(listSquareData: MutableList<SquareData>) {
        refreshView(listSquareData)
    }

    private fun openTouchPad(constraintConnectData: MutableList<ConstraintConnectData>): Boolean {
        if (!isOpen) {
            animateGrid(
                stateSubLevelClose, stateSubLevelOpen, stateOpenSquare, stateCloseSquare,
                constraintConnectData, constraintCenter, false
            )
            lastConstraintConnectDataUsed = constraintConnectData
            isOpen = !isOpen
            return true
        }
        return false
    }

    fun closeTouchPad(): Boolean {
        var result = false
        if (isLevel) {
            result = (subLevelView as TouchPadView).closeTouchPad()
        }
        if (isOpen && !result) {
            touchPadListener?.onUnSelectIdGrid(idGridSelected)
            animateGrid(
                stateSubLevelOpen, stateSubLevelClose, stateCloseSquare, stateOpenSquare,
                constraintCenter, lastConstraintConnectDataUsed!!, true
            )
            isOpen = !isOpen
            return true
        }
        return false
    }

    private fun animateGrid(
        stateStart: StateViewData,
        stateEnd: StateViewData,
        stateStartSquare: StateViewData,
        stateEndSquare: StateViewData,
        idStart: List<ConstraintConnectData>,
        idEnd: List<ConstraintConnectData>,
        isClose: Boolean
    ) {
        val startSet: ConstraintSet = getConstraintSet(squareTransition!!.startConstraintSetId)
        updateConstraintSet(startSet, stateStart, stateStartSquare, idStart)

        val endSet: ConstraintSet = getConstraintSet(squareTransition!!.endConstraintSetId)
        updateConstraintSet(endSet, stateEnd, stateEndSquare, idEnd)

        setTransition(
            squareTransition!!.startConstraintSetId,
            squareTransition!!.endConstraintSetId
        )

        transitionToEnd()

        // Update the end state to be the current.
        if (isClose) {
            startSet.clone(this)
            endSet.clone(this)
        }
    }

    private fun updateConstraintSet(
        set: ConstraintSet,
        state: StateViewData,
        stateSquare: StateViewData,
        listConstraint: List<ConstraintConnectData>
    ) {
        set.clone(this)

        set.clear(subLevelView.id, ConstraintSet.BOTTOM)
        set.clear(subLevelView.id, ConstraintSet.RIGHT)

        set.setAlpha(subLevelView.id, state.alpha)
        set.setScaleX(subLevelView.id, state.scale)
        set.setScaleY(subLevelView.id, state.scale)

        listConstraint.forEach {
            set.connect(subLevelView.id, it.startSide, it.endId, it.endSide, 0)
        }

        listIdRes.forEach {
            set.setAlpha(it, stateSquare.alpha)
            set.setScaleX(it, stateSquare.scale)
            set.setScaleY(it, stateSquare.scale)
        }
    }

}

interface TouchPadListener {
    fun onSelectIdGrid(idGrid: Int)
    fun onSelectIdSquare(idSquare: Int)
    fun onSelectValue(value: Int)

    fun onUnSelectIdGrid(idGrid: Int)
    fun onUnSelectIdSquare(idSquare: Int)
}

data class StateViewData(val alpha: Float, val scale: Float)

data class ConstraintConnectData(val startSide: Int, val endId: Int, val endSide: Int)