package com.lconsulting.sudoku.ui.main

import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object SudokuViewModelTest : Spek({

    val viewModel by memoized { SudokuViewModel() }

    Feature("SudokuViewModel") {

        Scenario("get index grid") {
            var index = 0
            var indexGrid = 0
            var result = 8

            Given("initial index to 70") {
                index = 70
            }

            When("compute grid index") {
                val method = viewModel.javaClass.getDeclaredMethod("getIndexGrid", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = index
                indexGrid = method.invoke(viewModel, *parameters) as Int
            }

            Then("it should return indexGrid to 8") {
                assertEquals(result, indexGrid)
            }
        }

        Scenario("get index with square index & grill index") {
            var idGrid = 0
            var idSquare = 0
            var index = 0
            var result = 50

            Given("initial idGrid to 4 & idSquare to 8") {
                idGrid = 4
                idSquare = 8
            }

            When("compute index") {
                val method = viewModel.javaClass.getDeclaredMethod(
                    "getIndex",
                    Int::class.java,
                    Int::class.java
                )
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(2)
                parameters[0] = idGrid
                parameters[1] = idSquare
                index = method.invoke(viewModel, *parameters) as Int
            }

            Then("it should return index to 50") {
                assertEquals(result, index)
            }
        }

        Scenario("get first index square in grid who contains index") {
            var index = 0
            var indexResult = 0
            var result = 60

            Given("initial index to 70") {
                index = 70
            }

            When("compute start index grid") {
                val method =
                    viewModel.javaClass.getDeclaredMethod("getStartIndexGrid", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = index
                indexResult = method.invoke(viewModel, *parameters) as Int
            }

            Then("it should return start index grid to 60") {
                assertEquals(result, indexResult)
            }
        }

        Scenario("get first index in column who contains index") {
            var index = 0
            var indexResult = 0
            var result = 3

            Given("initial index to 39") {
                index = 39
            }

            When("compute start index column") {
                val method =
                    viewModel.javaClass.getDeclaredMethod("getStartIndexColumn", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = index
                indexResult = method.invoke(viewModel, *parameters) as Int
            }

            Then("it should return star index column to 39") {
                assertEquals(result, indexResult)
            }
        }

        Scenario("get first index in row who contains index") {
            var index = 0
            var indexResult = 0
            var result = 36

            Given("initial index to 39") {
                index = 39
            }

            When("compute start index row") {
                val method =
                    viewModel.javaClass.getDeclaredMethod("getStartIndexRow", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = index
                indexResult = method.invoke(viewModel, *parameters) as Int
            }

            Then("it should return  start index row to 36") {
                assertEquals(result, indexResult)
            }
        }

        Scenario("get index in grid with position") {
            var index = 0
            var position = 0
            var indexResult = 0
            var result = 38

            Given("initial index to 27 & position to 5") {
                index = 27
                position = 5
            }

            When("compute index in grid") {
                val method = viewModel.javaClass.getDeclaredMethod(
                    "getIndexInGrid",
                    Int::class.java,
                    Int::class.java
                )
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(2)
                parameters[0] = index
                parameters[1] = position
                indexResult = method.invoke(viewModel, *parameters) as Int
            }

            Then("it should return  start index row to 37") {
                assertEquals(result, indexResult)
            }
        }

    }

    group("getIndexSquareInGrid spek") {

        listOf(
            1 to 1,
            12 to 3,
            40 to 4,
            44 to 5,
            60 to 0
        ).forEach {
            test("index in grid for this index ${it.first} in sudoku is ${it.second}") {
                val method = viewModel.javaClass.getDeclaredMethod("getIndexSquareInGrid", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = it.first
                val result = method.invoke(viewModel, *parameters) as Int
                assertEquals(it.second, result)
            }
        }
    }

    describe("getIndexSquareInGrid") {

        listOf(
            1 to 1,
            12 to 3,
            40 to 4,
            44 to 5,
            60 to 0
        ).forEach {
            it("index in grid for this index ${it.first} in sudoku is ${it.second}") {
                val method = viewModel.javaClass.getDeclaredMethod("getIndexSquareInGrid", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = it.first
                val result = method.invoke(viewModel, *parameters) as Int
                assertEquals(it.second, result)
            }
        }
    }

    describe("getIndexInRow") {
        listOf(
            (0 to 0) to 0,
            (36 to 3) to 39,
            (63 to 5) to 68
        ).forEach {
            it("with start Index to  ${it.first.first} & position ${it.first.second} to, index is ${it.second}") {
                val method = viewModel.javaClass.getDeclaredMethod("getIndexInRow", Int::class.java, Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(2)
                parameters[0] = it.first.first
                parameters[1] = it.first.second
                val result = method.invoke(viewModel, *parameters) as Int
                assertEquals(it.second, result)
            }
        }
    }

    describe("getIndexInColumn") {
        listOf(
            (0 to 0) to 0,
            (4 to 3) to 31,
            (8 to 5) to 53
        ).forEach {
            it("with start Index to  ${it.first.first} & position ${it.first.second} to, index is ${it.second}") {
                val method = viewModel.javaClass.getDeclaredMethod("getIndexInColumn", Int::class.java, Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(2)
                parameters[0] = it.first.first
                parameters[1] = it.first.second
                val result = method.invoke(viewModel, *parameters) as Int
                assertEquals(it.second, result)
            }
        }
    }

    describe("getStartIndexRowByPosition") {
        listOf(
            0 to 0,
            4 to 36,
            6 to 54
        ).forEach {
            it("with position to  ${it.first}, start index row is ${it.second}") {
                val method = viewModel.javaClass.getDeclaredMethod("getStartIndexRowByPosition", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = it.first
                val result = method.invoke(viewModel, *parameters) as Int
                assertEquals(it.second, result)
            }
        }
    }

    describe("getStartIndexGridByPosition") {
        listOf(
            0 to 0,
            4 to 30,
            6 to 54
        ).forEach {
            it("with position to  ${it.first}, start index grid  is ${it.second}") {
                val method = viewModel.javaClass.getDeclaredMethod("getStartIndexGridByPosition", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = it.first
                val result = method.invoke(viewModel, *parameters) as Int
                assertEquals(it.second, result)
            }
        }
    }

    describe("getStartIndexColumnByPosition") {
        listOf(
            0 to 0,
            4 to 4,
            6 to 6
        ).forEach {
            it("with position to  ${it.first}, start index column is ${it.second}") {
                val method = viewModel.javaClass.getDeclaredMethod("getStartIndexColumnByPosition", Int::class.java)
                method.isAccessible = true
                val parameters = arrayOfNulls<Any>(1)
                parameters[0] = it.first
                val result = method.invoke(viewModel, *parameters) as Int
                assertEquals(it.second, result)
            }
        }
    }

//    describe("removeValuePair") {
//        before {
//
//        }
//        listOf(
//            0 to 0,
//            4 to 4,
//            6 to 6
//        ).forEach {
//            it("remove value from indexPair on index") {
//                val method = viewModel.javaClass.getDeclaredMethod("removeValuePair", Int::class.java, Int::class.java)
//                method.isAccessible = true
//                val parameters = arrayOfNulls<Any>(1)
//                parameters[0] = it.first
//                parameters[1] = it.second
//                val result = method.invoke(viewModel, *parameters) as Boolean
//                assertEquals(it.second, result)
//            }
//        }
//    }


})