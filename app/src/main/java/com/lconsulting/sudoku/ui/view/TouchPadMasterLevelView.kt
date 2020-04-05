package com.lconsulting.sudoku.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.core.view.forEach
import com.lconsulting.sudoku.data.SquareData

class TouchPadMasterLevelView : TouchPadView {

    private var mTouchPadViewListener: TouchPadViewListener? = null

    private var idGridSelected = 0


    private var mIsOpeningForTransition = false
    private var mIdSquareForTransition = -1
    private var mIsClosingForTransition = false


    private val onTouchPadViewListener = object : TouchPadViewListener {
        override fun onSelectIdGrid(idGrid: Int) {
        }

        override fun onSelectIdSquare(idSquare: Int) {
            mTouchPadViewListener?.onSelectIdSquare(idSquare)
        }

        override fun onSelectValue(value: Int) {

        }

        override fun onUnSelectIdGrid(idGrid: Int) {

        }

        override fun onUnSelectIdSquare(idSquare: Int) {
            mTouchPadViewListener?.onUnSelectIdSquare(idSquare)
        }

        override fun onOpenSubTouchBar() {

        }

        override fun onCloseSubTouchBar() {
            //do nothing
        }

    }

//    private val onTransitionListener = object : TransitionListener {
//        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
//            //nothing to do
//        }
//
//        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
//            //nothing to do
//        }
//
//        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//            //nothing to do
//        }
//
//        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
//            if(mIsOpeningForTransition && mIdSquareForTransition != -1){
//                touchPadListener?.onOpenSubTouchBar()
//                mIsOpeningForTransition = false
//                mIdSquareForTransition = -1
//            }
//        }
//    }

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    override fun initValues() {
        forEach {
            when (it) {
                is TouchPadSubLevelView -> {
                    subLevelView = it
                    it.setTouchPadViewListener(onTouchPadViewListener)
                }
                else -> initValue(it)
            }
        }
    }

    override fun createOnClickListener() {
        mOnClickListener = OnClickListener {
            when (it) {
                is SquareView -> {
                    idGridSelected = it.tag.toString().toInt() - 1
                    mTouchPadViewListener?.onSelectIdGrid(idGridSelected)
                }
            }
        }
    }

    fun setTouchPadViewListener(listener: TouchPadViewListener) {
        mTouchPadViewListener = listener
    }

    fun open(listSquareData: MutableList<SquareData>) {
        (subLevelView as TouchPadSubLevelView).refreshValues(listSquareData)
        openTouchPad(listConstraint[idGridSelected])
    }

    fun close() {
        if (isOpen()) {
            if ((subLevelView as TouchPadSubLevelView).isOpen()) {
                (subLevelView as TouchPadSubLevelView).close()
            } else {
                mTouchPadViewListener?.onUnSelectIdGrid(idGridSelected)
                cloTouchPad()
            }
        }
    }

//    fun setPrepareOpening(idGrid: Int, idSquareForTransition: Int) {
//        idGridSelected = idGrid
//        mIsOpeningForTransition = true
//        mIdSquareForTransition = idSquareForTransition
//    }
//
//    fun setPrepareClosing() {
//        mIsClosingForTransition = true
//    }
//

//
//    fun openSubTouchPad(idSquare: Int) {
//        (subLevelView as TouchPadMasterLevelView).refreshSubView(idSquare)
//    }
//

//
//    private fun refreshSubView(idSquare: Int) {
//        idGridSelected = idSquare
//        (subLevelView as GridView).refreshView(listSquareData!![idSquare].possibility)
//        openTouchPad(listConstraint[idGridSelected])
//    }
//
//    fun refreshTouchPad(listSquareData: MutableList<SquareData>) {
//        (subLevelView as TouchPadMasterLevelView).refreshSubTouchPad(listSquareData)
//    }
//
//    private fun refreshSubTouchPad(listSquareData: MutableList<SquareData>) {
//        refreshView(listSquareData)
//    }
//


//    fun closeTouchPad(): Boolean {
//        var result = false
//        if (isLevel) {
//            result = (subLevelView as TouchPadMasterLevelView).closeTouchPad()
//        }
//        if (isOpen && !result) {
//            touchPadListener?.onUnSelectIdGrid(idGridSelected)
//            animateGrid(
//                stateSubLevelOpen, stateSubLevelClose, stateCloseSquare, stateOpenSquare,
//                constraintCenter, lastConstraintConnectDataUsed!!, true
//            )
//            isOpen = !isOpen
//            return true
//        }
//        return false
//    }


}