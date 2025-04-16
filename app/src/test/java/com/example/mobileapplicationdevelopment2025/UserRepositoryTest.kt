package com.example.mobileapplicationdevelopment2025

import com.example.mobileapplicationdevelopment2025.fakes.FakeApiService
import com.example.mobileapplicationdevelopment2025.fakes.FakeUserDao
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.repository.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var fakeDao: FakeUserDao
    private lateinit var fakeApi: FakeApiService
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        fakeDao = FakeUserDao()
        fakeApi = FakeApiService()
        repository = UserRepository(fakeDao, fakeApi)
    }

    private fun sampleUser() = User(
        id = "user123",
        first_name = "Test",
        last_name = "User",
        email = "test@example.com",
        username = "testuser",
        password = "password123"
    )

    @Test
    fun `registerUserRemote returns success response`() = runTest {
        fakeApi.simulateSuccess = true
        val response = repository.registerUserRemote(sampleUser())
        assertTrue(response.isSuccessful)
        assertEquals("user123", response.body()?.user_id)
    }

    @Test
    fun `cacheUser stores user correctly`() = runTest {
        val user = sampleUser()
        repository.cacheUser(user)
        val cached = repository.getCachedUser()
        assertEquals(user.id, cached?.id)
    }

    @Test
    fun `clearOtherUsers removes users not matching id`() = runTest {
        val user = sampleUser()
        repository.cacheUser(user)
        repository.clearOtherUsers("different_id")
        val result = repository.getCachedUser()
        assertNull(result)
    }

    @Test
    fun `registerUserRemote returns error response`() = runTest {
        fakeApi.simulateSuccess = false // simulate error
        val response = repository.registerUserRemote(sampleUser())
        assertFalse(response.isSuccessful)
        assertEquals(500, response.code())
    }

    @Test
    fun `clearOtherUsers keeps current user if id matches`() = runTest {
        val user = sampleUser()
        repository.cacheUser(user)
        repository.clearOtherUsers("user123") // same ID
        val result = repository.getCachedUser()
        assertNotNull(result) // should still exist
        assertEquals("user123", result?.id)
    }


}
