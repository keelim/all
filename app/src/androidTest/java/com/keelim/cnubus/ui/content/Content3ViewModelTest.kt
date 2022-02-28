package com.keelim.cnubus.ui.content

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class Content3ViewModelTest {
    private lateinit var viewModel:Content3ViewModel

    @Before
    fun setUpViewModel(){
        viewModel = Content3ViewModel()
    }

    @Test
    fun start1() = runBlocking {
        viewModel.start1()
        val value = viewModel.viewEvent.value.getContentIfNotHandled()
        assertEquals(value, "")
    }
}