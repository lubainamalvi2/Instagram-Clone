package com.example.mobileapplicationdevelopment2025

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobileapplicationdevelopment2025.fakes.FakeLoginRepository
import com.example.mobileapplicationdevelopment2025.viewmodel.ForgotPasswordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var fakeLoginRepo: FakeLoginRepository

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeLoginRepo = FakeLoginRepository()
        viewModel = ForgotPasswordViewModel(fakeLoginRepo)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendResetCode returns success when API is successful`() = runTest {
        fakeLoginRepo.simulateSendResetCodeSuccess = true
        fakeLoginRepo.simulateSendResetCodeError = false
        fakeLoginRepo.simulateSendResetCodeThrowsException = false

        viewModel.sendResetCode("test@email.com")
        advanceUntilIdle()
        assertEquals("success", viewModel.sendCodeResult.first())
    }

    @Test
    fun `sendResetCode returns error when API returns error body`() = runTest {
        fakeLoginRepo.simulateSendResetCodeSuccess = false
        fakeLoginRepo.simulateSendResetCodeError = true
        fakeLoginRepo.simulateSendResetCodeThrowsException = false

        viewModel.sendResetCode("invalid@email.com")
        advanceUntilIdle()
        assertEquals("Incorrect password", viewModel.sendCodeResult.first())
    }

    @Test
    fun `sendResetCode handles network exception`() = runTest {
        fakeLoginRepo.simulateSendResetCodeSuccess = false
        fakeLoginRepo.simulateSendResetCodeError = false
        fakeLoginRepo.simulateSendResetCodeThrowsException = true

        viewModel.sendResetCode("offline@email.com")
        advanceUntilIdle()
        assertTrue(viewModel.sendCodeResult.first()?.contains("Network error") == true)
    }

}
