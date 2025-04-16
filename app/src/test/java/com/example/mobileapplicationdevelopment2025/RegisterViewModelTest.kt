package com.example.mobileapplicationdevelopment2025

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobileapplicationdevelopment2025.fakes.FakeApplication
import com.example.mobileapplicationdevelopment2025.fakes.FakeUserRepository
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.viewmodel.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: RegisterViewModel
    private lateinit var fakeRepo: FakeUserRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val fakeApp = FakeApplication()
        fakeRepo = FakeUserRepository()
        viewModel = RegisterViewModel(application = fakeApp, repository = fakeRepo)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createUser(
        first: String = "Test",
        last: String = "User",
        email: String = "test@email.com",
        username: String = "testuser",
        password: String = "password123"
    ) = User(
        id = "",
        first_name = first,
        last_name = last,
        email = email,
        username = username,
        password = password
    )

    @Test
    fun `registration succeeds when data is valid`() = runTest {
        fakeRepo.simulateSuccess = true
        viewModel.register(createUser(), "password123")
        advanceUntilIdle()
        val result = viewModel.registerResult.first()
        println("Result: $result")
        assertTrue(result?.contains("success", true) == true)
    }

    @Test
    fun `registration fails when passwords don't match`() = runTest {
        viewModel.register(createUser(), "mismatch")
        advanceUntilIdle()
        val result = viewModel.registerResult.first()
        assertEquals("Passwords do not match.", result)
    }

    @Test
    fun `registration fails with invalid email`() = runTest {
        viewModel.register(createUser(email = "invalidemail"), "password123")
        advanceUntilIdle()
        val result = viewModel.registerResult.first()
        assertEquals("Invalid email format.", result)
    }

    @Test
    fun `registration fails with missing fields`() = runTest {
        viewModel.register(createUser(first = ""), "password123")
        advanceUntilIdle()
        val result = viewModel.registerResult.first()
        assertEquals("All fields are required.", result)
    }

    @Test
    fun `registration fails with duplicate username`() = runTest {
        fakeRepo.simulateSuccess = false
        fakeRepo.simulateDuplicate = true
        viewModel.register(createUser(), "password123")
        advanceUntilIdle()
        val result = viewModel.registerResult.first()
        assertEquals("Something went wrong", result)
    }
}
