package com.example.mobileapplicationdevelopment2025

import com.example.mobileapplicationdevelopment2025.fakes.FakeApiService
import com.example.mobileapplicationdevelopment2025.repository.LoginRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import retrofit2.Response
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepositoryTest {

    private lateinit var fakeApi: FakeApiService
    private lateinit var repository: LoginRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeApi = FakeApiService()
        repository = LoginRepository(fakeApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ------------------- LOGIN -------------------
    @Test
    fun `login succeeds with correct credentials`() = runTest {
        fakeApi.simulateSuccess = true
        val result = repository.login("testuser", "password")
        assertTrue(result.isSuccessful)
        assertEquals("user123", result.body()?.user_id)
    }

    @Test
    fun `login fails with invalid credentials`() = runTest {
        fakeApi.simulateSuccess = false
        val result = repository.login("wrong", "wrong")
        assertFalse(result.isSuccessful)
        assertEquals(401, result.code())
    }

    // ------------------- RESET CODE -------------------
    @Test
    fun `sendResetCode returns success`() = runTest {
        fakeApi.simulateSuccess = true
        val result = repository.sendResetCode("test@email.com")
        assertTrue(result.isSuccessful)
    }

    @Test
    fun `sendResetCode returns error`() = runTest {
        fakeApi.simulateSuccess = false
        val result = repository.sendResetCode("test@email.com")
        assertFalse(result.isSuccessful)
        assertEquals(500, result.code())
    }

    // ------------------- VERIFY CODE -------------------
    @Test
    fun `verifyResetCode returns success`() = runTest {
        fakeApi.simulateSuccess = true
        val result = repository.verifyResetCode("test@email.com", "123456")
        assertEquals("success", result)
    }

    @Test
    fun `verifyResetCode returns failure`() = runTest {
        fakeApi.simulateSuccess = false
        val result = repository.verifyResetCode("test@email.com", "wrongcode")
        assertEquals("Invalid or expired code", result)
    }

    // ------------------- RESET PASSWORD -------------------
    @Test
    fun `resetPassword returns success`() = runTest {
        fakeApi.simulateSuccess = true
        val result = repository.resetPassword("test@email.com", "123456", "newpass")
        assertEquals("success", result)
    }

    @Test
    fun `resetPassword returns failure`() = runTest {
        fakeApi.simulateSuccess = false
        val result = repository.resetPassword("test@email.com", "wrongcode", "newpass")
        assertEquals("Reset failed", result)
    }
}
