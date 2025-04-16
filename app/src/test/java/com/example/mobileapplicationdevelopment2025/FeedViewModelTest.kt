package com.example.mobileapplicationdevelopment2025

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobileapplicationdevelopment2025.fakes.FakeApiService
import com.example.mobileapplicationdevelopment2025.model.Author
import com.example.mobileapplicationdevelopment2025.model.Comment
import com.example.mobileapplicationdevelopment2025.model.FeedPostsResponse
import com.example.mobileapplicationdevelopment2025.model.Post
import com.example.mobileapplicationdevelopment2025.viewmodel.FeedState
import com.example.mobileapplicationdevelopment2025.viewmodel.FeedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeApi: FakeApiService
    private lateinit var feedViewModel: FeedViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeApi = FakeApiService().apply {
            storedFeedResponse = FeedPostsResponse(posts = emptyList(), count = 0)
            likeCount = 0
            simulateSuccess = true
        }
        feedViewModel = FeedViewModel(api = fakeApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createPost(
        id: String = "post123",
        accountId: String = "user123",
        imageUrl: String = "https://example.com/image.jpg",
        caption: String = "Test caption",
        timestamp: String = "Tue, 08 Apr 2025 20:35:13 GMT",
        likes: Int = 0,
        isLiked: Boolean = false,
        commentDetails: List<Comment>? = emptyList()
    ) = Post(
        id = id,
        accountId = accountId,
        imageUrl = imageUrl,
        caption = caption,
        timestamp = timestamp,
        likes = likes,
        author = Author(id = accountId, username = "testuser", pfp = null),
        commentDetails = commentDetails,
        isLiked = isLiked
    )

    @Test
    fun `fetchFeedPosts loads posts successfully`() = runTest {
        val samplePosts = listOf(
            createPost(id = "post1"),
            createPost(id = "post2")
        )
        fakeApi.storedFeedResponse = FeedPostsResponse(posts = samplePosts, count = samplePosts.size)
        fakeApi.simulateSuccess = true
        feedViewModel.fetchFeedPosts("user123")
        advanceUntilIdle()
        val state: FeedState = feedViewModel.feedState.first()
        assertEquals(2, state.posts.size)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `fetchFeedPosts shows error on failure`() = runTest {
        fakeApi.simulateSuccess = false
        feedViewModel.fetchFeedPosts("user123")
        advanceUntilIdle()

        val state: FeedState = feedViewModel.feedState.first()
        assertTrue(state.posts.isEmpty())
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `updateLikeForPost returns updated like count on like operation`() = runTest {
        fakeApi.simulateSuccess = true
        fakeApi.likeCount = 4
        var callbackCalled = false
        feedViewModel.updateLikeForPost("post1", true, "user123") { newIsLiked, newLikesCount ->
            callbackCalled = true
            assertTrue(newIsLiked)
            assertEquals(5, newLikesCount)
        }
        advanceUntilIdle()
        assertTrue(callbackCalled)
    }

    @Test
    fun `updateLikeForPost returns fallback on failure`() = runTest {
        fakeApi.simulateSuccess = false

        var callbackCalled = false
        feedViewModel.updateLikeForPost("post1", true, "user123") { newIsLiked, newLikesCount ->
            callbackCalled = true
            assertFalse(newIsLiked)
            assertEquals(0, newLikesCount)
        }
        advanceUntilIdle()
        assertTrue(callbackCalled)
    }

    @Test
    fun `addCommentToPost returns success on comment addition`() = runTest {
        fakeApi.simulateSuccess = true
        var result = false
        feedViewModel.addCommentToPost("post1", "user123", "Great post!") { success ->
            result = success
        }
        advanceUntilIdle()
        assertTrue(result)
    }

    @Test
    fun `addCommentToPost returns false on failure`() = runTest {
        fakeApi.simulateSuccess = false
        var result = true
        feedViewModel.addCommentToPost("post1", "user123", "Great post!") { success ->
            result = success
        }
        advanceUntilIdle()
        assertFalse(result)
    }

    @Test
    fun `fetchFeedPosts loads empty feed when no posts exist`() = runTest {
        fakeApi.storedFeedResponse = FeedPostsResponse(posts = emptyList(), count = 0)
        fakeApi.simulateSuccess = true
        feedViewModel.fetchFeedPosts("user123")
        advanceUntilIdle()
        val state: FeedState = feedViewModel.feedState.first()
        assertTrue(state.posts.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `updateLikeForPost returns updated like count on unlike operation`() = runTest {
        fakeApi.simulateSuccess = true
        fakeApi.likeCount = 10
        var callbackCalled = false
        feedViewModel.updateLikeForPost("post1", false, "user123") { newIsLiked, newLikesCount ->
            callbackCalled = true
            assertFalse(newIsLiked)
            assertEquals(9, newLikesCount)
        }
        advanceUntilIdle()
        assertTrue(callbackCalled)
    }

    @Test
    fun `fetchFeedPosts with profile feed returns only posts for that profile`() = runTest {
        val samplePosts = listOf(
            createPost(id = "post1", accountId = "user123"),
            createPost(id = "post2", accountId = "otherUser"),
            createPost(id = "post3", accountId = "user123")
        )
        fakeApi.storedPosts = samplePosts
        fakeApi.simulateSuccess = true
        feedViewModel.fetchFeedPosts("user123", feedType = "profile")
        advanceUntilIdle()

        val state: FeedState = feedViewModel.feedState.first()
        assertEquals(2, state.posts.size)
        state.posts.forEach { post ->
            assertEquals("user123", post.accountId)
        }
    }
}