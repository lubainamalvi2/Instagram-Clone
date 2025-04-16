package com.example.mobileapplicationdevelopment2025.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobileapplicationdevelopment2025.fakes.FakeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeApi: FakeApiService
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeApi = FakeApiService()
        viewModel = SearchViewModel(api = fakeApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchQueryChanged updates search query state`() = runTest {
        val query = "hello"
        viewModel.onSearchQueryChanged(query)
        assertEquals(query, viewModel.searchQuery.first())
    }

    @Test
    fun `executeSearch success updates searchResults`() = runTest {
        fakeApi.simulateSuccess = true
        viewModel.onSearchQueryChanged("test")
        viewModel.executeSearch()
        advanceUntilIdle()

        val results = viewModel.searchResults.first()
        assertEquals(1, results.size)
        assertNull(viewModel.errorMessage.first())
        assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun `executeSearch error sets errorMessage`() = runTest {
        fakeApi.simulateSuccess = false
        viewModel.onSearchQueryChanged("error")
        viewModel.executeSearch()
        advanceUntilIdle()

        assertTrue(viewModel.searchResults.first().isEmpty())
        val errorMsg = viewModel.errorMessage.first()
        assertNotNull(errorMsg)
        assertTrue(errorMsg!!.contains("Error") || errorMsg.contains("Error occurred"))
    }
}
