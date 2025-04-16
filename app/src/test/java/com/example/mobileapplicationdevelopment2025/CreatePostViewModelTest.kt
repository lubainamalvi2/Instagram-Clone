package com.example.mobileapplicationdevelopment2025

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobileapplicationdevelopment2025.fakes.FakeApiService
import com.example.mobileapplicationdevelopment2025.fakes.FakeCloudinaryApiService
import com.example.mobileapplicationdevelopment2025.fakes.FakeUserDao
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.viewmodel.CreatePostViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*

import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePostViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeCloudinaryApi: FakeCloudinaryApiService
    private lateinit var fakeApi: FakeApiService
    private lateinit var fakeUserDao: FakeUserDao
    private lateinit var viewModel: CreatePostViewModel

    private class FakeApplication : android.app.Application()

    private lateinit var fakeApplication: FakeApplication

    private fun createTestUser(): User {
        return User(
            id = "user_test",
            pfp = null,
            first_name = "Test",
            last_name = "User",
            username = "testuser",
            email = "test@example.com",
            password = "password123",
            bio = "Test bio",
            links = emptyMap(),
            followers = emptyList(),
            following = emptyList()
        )
    }

    @Before
    fun setup() = runTest {
        Dispatchers.setMain(testDispatcher)
        fakeApplication = FakeApplication()
        fakeCloudinaryApi = FakeCloudinaryApiService().apply { simulateSuccess = true }
        fakeApi = FakeApiService().apply { simulateSuccess = true }
        fakeUserDao = FakeUserDao()

        fakeUserDao.insertUser(createTestUser())

        viewModel = CreatePostViewModel(
            application = fakeApplication,
            cloudinaryApi = fakeCloudinaryApi,
            api = fakeApi,
            userDao = fakeUserDao
        )
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createPost returns error when no image selected`() = runTest {
        viewModel.setSelectedImagePath(null)
        viewModel.createPost { }
        advanceUntilIdle()
        assertEquals("Please select or take an image.", viewModel.errorMessage.first())
    }

    @Test
    fun `createPost returns error when cloudinary upload fails`() = runTest {
        viewModel.setSelectedImagePath("dummy_path")
        fakeCloudinaryApi.simulateSuccess = false
        viewModel.createPost { }
        advanceUntilIdle()
        assertTrue(viewModel.errorMessage.first()?.contains("Cloudinary upload error") == true)
    }

    @Test
    fun `createPost returns error when backend API fails`() = runTest {
        viewModel.setSelectedImagePath("dummy_path")
        fakeCloudinaryApi.simulateSuccess = true
        fakeCloudinaryApi.secureUrlToReturn = "https://res.cloudinary.com/dtak8hvhi/image/upload/v123456/sample.jpg"
        fakeApi.simulateSuccess = false
        viewModel.createPost { }
        advanceUntilIdle()
        assertTrue(viewModel.errorMessage.first()?.contains("Error:") == true)
    }

    @Test
    fun `createPost succeeds and calls onSuccess`() = runTest {
        viewModel.setSelectedImagePath("dummy_path")
        fakeCloudinaryApi.simulateSuccess = true
        fakeCloudinaryApi.secureUrlToReturn = "https://res.cloudinary.com/dtak8hvhi/image/upload/v123456/sample.jpg"
        fakeApi.simulateSuccess = true
        var onSuccessCalled = false
        viewModel.createPost {
            onSuccessCalled = true
        }
        advanceUntilIdle()
        assertTrue(onSuccessCalled)
        assertNull(viewModel.errorMessage.first())
    }
}
