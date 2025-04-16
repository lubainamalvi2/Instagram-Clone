package com.example.mobileapplicationdevelopment2025.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationdevelopment2025.model.Post
import com.example.mobileapplicationdevelopment2025.model.User
import com.example.mobileapplicationdevelopment2025.network.RetrofitInstance
import com.example.mobileapplicationdevelopment2025.repository.IUserRepository
import com.example.mobileapplicationdevelopment2025.repository.UserRepository
import com.example.mobileapplicationdevelopment2025.room.UserDao
import com.example.mobileapplicationdevelopment2025.room.UserDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.example.mobileapplicationdevelopment2025.network.ApiService
import com.example.mobileapplicationdevelopment2025.util.Logger
import com.example.mobileapplicationdevelopment2025.util.AndroidLogger

class ProfileViewModel(
    application: Application,
    private val repository: IUserRepository = UserRepository(application),
    private val userDao: UserDao = UserDatabase.getDatabase(application).userDao(),
    private val api: ApiService = RetrofitInstance.api,
    private val logger: Logger = AndroidLogger()
) : AndroidViewModel(application) {
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _postCount = MutableStateFlow(0)
    val postCount: StateFlow<Int> = _postCount

    private val _followerCount = MutableStateFlow(0)
    val followerCount: StateFlow<Int> = _followerCount

    private val _followingCount = MutableStateFlow(0)
    val followingCount: StateFlow<Int> = _followingCount

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var loggedInUserId: String? = null

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            val user = repository.getCachedUser()
            _currentUser.value = user
            loggedInUserId = user?.id
            
            // Update follower and following counts
            _followerCount.value = user?.followers?.size ?: 0
            _followingCount.value = user?.following?.size ?: 0
            
            // Load posts for the current user if available
            user?.id?.let { userId ->
                logger.d("ProfileViewModel", "Loading posts for current user: $userId")
                loadUserPosts(userId)
            }
        }
    }
    
    fun refreshUserFromApi(userId: String) {
        viewModelScope.launch {
            try {
                val response = api.getUserById(userId)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _currentUser.value = user
                        // Update the cached user in Room
                        userDao.insertUser(user)
                    }
                } else {
                    logger.e("ProfileViewModel", "Failed to refresh user: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                logger.e("ProfileViewModel", "Error refreshing user", e)
            }
        }
    }

    fun isCurrentUser(userId: String?): Boolean {
        return userId == null || userId == loggedInUserId
    }

    fun loadUserById(userId: String) {
        viewModelScope.launch {
            try {
                val response = api.getUserById(userId)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _currentUser.value = user
                        // Update follower and following counts from the user data
                        _followerCount.value = user.followers?.size ?: 0
                        _followingCount.value = user.following?.size ?: 0
                        // Load posts for this user
                        loadUserPosts(userId)
                    }
                }
            } catch (e: Exception) {
                logger.e("ProfileViewModel", "Error loading user by ID", e)
            }
        }
    }

    fun loadUserPosts(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                logger.d("ProfileViewModel", "Loading posts for user: $userId")
                val response = api.getUserPosts(userId)
                if (response.isSuccessful) {
                    val postsList = response.body()?.posts ?: emptyList()
                    val count = response.body()?.count ?: 0
                    logger.d("ProfileViewModel", "Loaded ${postsList.size} posts, count: $count")
                    _posts.value = postsList
                    _postCount.value = count
                } else {
                    logger.e("ProfileViewModel", "Failed to load posts: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                logger.e("ProfileViewModel", "Error loading posts", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearOtherUsers("") // Clear all users by passing an empty string
            _currentUser.value = null
            _posts.value = emptyList()
            _postCount.value = 0
        }
    }

    fun updateUserProfile(updatedUser: User) {
        viewModelScope.launch {
            try {
                // Create a map of the updated user data
                val userData = mutableMapOf<String, String>().apply {
                    updatedUser.first_name?.let { put("first_name", it) }
                    updatedUser.last_name?.let { put("last_name", it) }
                    updatedUser.username?.let { put("username", it) }
                    updatedUser.bio?.let { put("bio", it) }
                    // Convert links map to JSON string
                    updatedUser.links?.let { links ->
                        if (links.isNotEmpty()) {
                            // Format links to ensure they start with https://www.
                            val formattedLinks = links.mapValues { (_, url) ->
                                when {
                                    url.startsWith("http://") -> url.replace("http://", "https://")
                                    url.startsWith("https://") -> url
                                    url.startsWith("www.") -> "https://$url"
                                    else -> "https://www.$url"
                                }
                            }
                            val linksJson = Gson().toJson(formattedLinks)
                            put("links", linksJson)
                        }
                    }
                }

                // Update local database and UI state immediately
                userDao.updateUser(updatedUser)
                _currentUser.value = updatedUser

                // Make the API call
                val response = api.updateUserProfile(updatedUser.id, userData)
                
                if (response.isSuccessful) {
                    // Refresh user data from API to ensure we have the latest data
                    refreshUserFromApi(updatedUser.id)
                    // Also reload posts to ensure everything is in sync
                    loadUserPosts(updatedUser.id)
                } else {
                    logger.e("ProfileViewModel", "Failed to update profile: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                logger.e("ProfileViewModel", "Error updating profile", e)
            }
        }
    }

    fun followUser(targetUserId: String) {
        viewModelScope.launch {
            try {
                loggedInUserId?.let { currentUserId ->
                    val response = api.followUser(currentUserId, targetUserId)
                    if (response.isSuccessful) {
                        // Refresh the user data to get updated follower counts
                        loadUserById(targetUserId)
                    } else {
                        logger.e("ProfileViewModel", "Failed to follow user: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                logger.e("ProfileViewModel", "Error following user", e)
            }
        }
    }

    fun unfollowUser(targetUserId: String) {
        viewModelScope.launch {
            try {
                loggedInUserId?.let { currentUserId ->
                    val response = api.unfollowUser(currentUserId, targetUserId)
                    if (response.isSuccessful) {
                        // Refresh the user data to get updated follower counts
                        loadUserById(targetUserId)
                    } else {
                        logger.e("ProfileViewModel", "Error unfollowing user: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                logger.e("ProfileViewModel", "Error unfollowing user", e)
            }
        }
    }

    fun getLoggedInUserId(): String? {
        return loggedInUserId
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 