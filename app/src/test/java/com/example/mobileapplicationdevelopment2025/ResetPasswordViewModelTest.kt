package com.example.mobileapplicationdevelopment2025

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobileapplicationdevelopment2025.fakes.FakeLoginRepository
import com.example.mobileapplicationdevelopment2025.viewmodel.ResetPasswordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ResetPasswordViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var fakeRepo: FakeLoginRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeLoginRepository()
        viewModel = ResetPasswordViewModel(repository = fakeRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `resetPassword fails when passwords do not match`() = runTest {
        viewModel.resetPassword(
            email = "test@email.com",
            code = "123456",
            newPassword = "abc123",
            confirmPassword = "xyz999"
        )
        assertEquals("Passwords do not match", viewModel.resetResult.first())
    }

    @Test
    fun `resetPassword returns success on valid input`() = runTest {
        fakeRepo.simulateSuccess = true
        viewModel.resetPassword(
            email = "test@email.com",
            code = "123456",
            newPassword = "password",
            confirmPassword = "password"
        )
        advanceUntilIdle()
        assertEquals("success", viewModel.resetResult.first())
    }

    @Test
    fun `resetPassword returns error from API`() = runTest {
        fakeRepo.simulateSuccess = false
        fakeRepo.simulateError = true
        viewModel.resetPassword(
            email = "test@email.com",
            code = "123456",
            newPassword = "password",
            confirmPassword = "password"
        )
        advanceUntilIdle()
        assertEquals("Incorrect password", viewModel.resetResult.first())
    }

    @Test
    fun `resetPassword catches exception`() = runTest {
        fakeRepo.simulateSuccess = false
        fakeRepo.simulateError = false
        viewModel.resetPassword(
            email = "test@email.com",
            code = "123456",
            newPassword = "password",
            confirmPassword = "password"
        )
        advanceUntilIdle()
        assertTrue(viewModel.resetResult.first()?.contains("Error") == true)
    }

    @Test
    fun `clearMessage sets resetResult to null`() = runTest {
        viewModel.clearMessage()
        assertNull(viewModel.resetResult.first())
    }
}
