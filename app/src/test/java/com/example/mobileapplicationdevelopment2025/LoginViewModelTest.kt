package com.example.mobileapplicationdevelopment2025

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobileapplicationdevelopment2025.fakes.FakeApiService
import com.example.mobileapplicationdevelopment2025.fakes.FakeApplication
import com.example.mobileapplicationdevelopment2025.fakes.FakeLoginRepository
import com.example.mobileapplicationdevelopment2025.fakes.FakeUserRepository
import com.example.mobileapplicationdevelopment2025.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeLoginRepo: FakeLoginRepository
    private lateinit var fakeUserRepo: FakeUserRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val fakeApp = FakeApplication()
        fakeLoginRepo = FakeLoginRepository()
        fakeUserRepo = FakeUserRepository()

        viewModel = LoginViewModel(
            application = fakeApp,
            loginRepo = fakeLoginRepo,
            userRepo = fakeUserRepo
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `clearResult sets loginResult to null`() = runTest {
        viewModel.clearResult()
        assertNull(viewModel.loginResult.first())
    }

    @Test
    fun `login with valid user sets success message`() = runTest {
        fakeLoginRepo.simulateSuccess = true
        viewModel.login("admin", "admin")
        advanceUntilIdle()
        val result = viewModel.loginResult.value
        assertEquals("Login successful!", result)
    }

    @Test
    fun `login with invalid credentials returns error message`() = runTest {
        fakeLoginRepo.simulateSuccess = false
        fakeLoginRepo.simulateError = true
        viewModel.login("wrong", "wrong")
        advanceUntilIdle()
        val result = viewModel.loginResult.value
        assertEquals("Incorrect password", result)
    }

    @Test
    fun `offline login fails when cached user does not match`() = runTest {
        fakeLoginRepo.simulateLoginThrowsException = true

        viewModel.login("nonexistent", "wrongpassword")
        advanceUntilIdle()

        val result = viewModel.loginResult.value
        assertEquals("Something went wrong. Please try again later.", result)
    }


    @Test
    fun `login result can be cleared after success`() = runTest {
        fakeLoginRepo.simulateSuccess = true
        viewModel.login("admin", "admin")
        advanceUntilIdle()
        viewModel.clearResult()
        val result = viewModel.loginResult.first()
        assertNull(result)
    }

    @Test
    fun `offline login succeeds when cached user matches`() = runTest {
        // Pre-cache a matching user
        val user = com.example.mobileapplicationdevelopment2025.model.User(
            id = "offline123",
            first_name = "Test",
            last_name = "User",
            username = "admin",
            email = "admin",
            password = "admin"
        )
        fakeUserRepo.cacheUser(user)

        fakeLoginRepo.simulateLoginThrowsException = true

        viewModel.login("admin", "admin")
        advanceUntilIdle()

        val result = viewModel.loginResult.value
        assertEquals("Login successful (offline)", result)
    }

    @Test
    fun `login with success response but null userId returns no result`() = runTest {
        fakeLoginRepo.simulateSuccess = true
        fakeLoginRepo.simulateNullUserId = true
        viewModel.login("admin", "admin")
        advanceUntilIdle()
        val result = viewModel.loginResult.value
        assertNull(result) // No userId, no result set
    }

    @Test
    fun `login sets minimal user if profile fetch fails`() = runTest {
        fakeLoginRepo.simulateSuccess = true
        viewModel.simulateUserProfileFetchFailure = true // Force profile fetch to fail

        viewModel.login("admin", "admin")
        advanceUntilIdle()

        val cachedUser = fakeUserRepo.lastCachedUser
        assertNotNull(cachedUser)
        assertEquals("admin", cachedUser?.username)
        assertEquals("admin", cachedUser?.email)
    }

    @Test
    fun `login with failed response sets failure message`() = runTest {
        fakeLoginRepo.simulateSuccess = false
        fakeLoginRepo.simulateError = false
        fakeLoginRepo.simulateLoginFailureResponse = true

        viewModel.login("admin", "admin")
        advanceUntilIdle()

        val result = viewModel.loginResult.value
        assertTrue(result?.startsWith("Login failed") == true)
    }

    @Test
    fun `login with successful response but with error body sets error message`() = runTest {
        fakeLoginRepo.simulateSuccess = true
        fakeLoginRepo.simulateErrorInBody = true

        viewModel.login("admin", "admin")
        advanceUntilIdle()

        val result = viewModel.loginResult.value
        assertEquals("Simulated server-side error", result)
    }

    @Test
    fun `login handles null user body after successful user fetch`() = runTest {
        fakeLoginRepo.simulateSuccess = true
        (viewModel as LoginViewModel).api = FakeApiService().apply {
            simulateSuccess = true
            simulateNullUserBody = true
        }

        viewModel.login("admin", "admin")
        advanceUntilIdle()

        val cachedUser = fakeUserRepo.lastCachedUser
        assertNull(cachedUser) // Nothing should be cached
        val result = viewModel.loginResult.value
        assertEquals("Login successful!", result) // Still reports success
    }

    @Test
    fun `login sets minimal user if profile fetch returns error response`() = runTest {
        fakeLoginRepo.simulateSuccess = true

        val fakeApi = FakeApiService().apply {
            simulateSuccess = false // simulate failed profile fetch
        }

        viewModel = LoginViewModel(
            application = FakeApplication(),
            loginRepo = fakeLoginRepo,
            userRepo = fakeUserRepo,
            api = fakeApi
        )

        viewModel.login("admin", "admin")
        advanceUntilIdle()

        val cachedUser = fakeUserRepo.lastCachedUser
        assertNotNull(cachedUser)
        assertEquals("admin", cachedUser?.username)
        assertEquals("admin", cachedUser?.email)
        val result = viewModel.loginResult.value
        assertEquals("Login successful!", result)
    }






}
