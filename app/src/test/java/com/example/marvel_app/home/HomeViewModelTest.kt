package com.example.marvel_app.home

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvel_app.MainCoroutineRule
import com.example.marvel_app.UIState
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApiService
import com.example.marvel_app.network.MarvelModule
import com.example.marvel_app.repository.ComicsRepository
import com.example.marvel_app.repository.FirebaseRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.lang.Exception
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class HomeViewModelTest {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    @Inject
    lateinit var comicsRepository: ComicsRepository

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var successTask: Task<AuthResult>


    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())

        successTask = object : Task<AuthResult>() {
            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = true

            // ...
            override fun addOnCompleteListener(
                executor: Executor,
                onCompleteListener: OnCompleteListener<AuthResult>
            ): Task<AuthResult> {
                onCompleteListener.onComplete(successTask)
                return successTask
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun getResult(): AuthResult? {
                TODO("Not yet implemented")
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult? {
                TODO("Not yet implemented")
            }

            override fun getException(): Exception? {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }
        }
        hiltRule.inject()
        homeViewModel = HomeViewModel(comicsRepository, firebaseRepository)
    }

    @Test
    fun testSetInSearchingToTrue() {
        homeViewModel.setInSearching(true)
        val inSearching: Boolean? = homeViewModel.inSearching.value
        if (inSearching != null) {
            assert(inSearching)
        } else {
            assert(false)
        }
    }

    @Test
    fun testSetInSearchingToFalse() {
        homeViewModel.setInSearching(false)
        val inSearching: Boolean? = homeViewModel.inSearching.value
        if (inSearching != null) {
            assert(!inSearching)
        } else {
            assert(false)
        }
    }

    @Test
    fun testDisplayComicDetail() {
        val comic = Comic(1, "Comic", "url", "ext", "desc", null, null)
        homeViewModel.displayComicDetail(comic)
        assert(homeViewModel.navigateToSelectedComic.value == comic)
    }

    @Test
    fun testDisplayComicDetailComplete() {
        val comic = Comic(1, "Comic", "url", "ext", "desc", null, null)
        homeViewModel.displayComicDetail(comic)
        homeViewModel.displayComicDetailComplete()
        assert(homeViewModel.navigateToSelectedComic.value == null)
    }

    @Test
    fun testInitFragmentForSearching() {
        homeViewModel.initFragmentForSearching()
        assert(homeViewModel.state.value == UIState.InSearching)
    }


    @Test
    fun testCheckIfUserSignedIn() {
        //every { firebaseRepository.user } returns null
        val b = homeViewModel.checkIfUserSignedIn()
        assert(!b)

    }

}