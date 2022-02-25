package com.smarttoolfactory.data.api

import com.google.common.truth.Truth
import com.smarttoolfactory.data.model.PostDTO
import com.smarttoolfactory.test_utils.extension.TestCoroutineExtension
import java.net.HttpURLConnection
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostApiCoroutinesTest : AbstractPostApiTest() {

    companion object {
        @JvmField
        @RegisterExtension
        val testCoroutineExtension = TestCoroutineExtension()

        val testCoroutineScope = testCoroutineExtension.testCoroutineScope
    }

    /**
     * Api is the SUT to test headers, url, response and DTO objects
     */
    private lateinit var api: PostApi

    /**
     * ❌ This test FAILS, with launch builder sometimes fail, sometimes PASS
     */
    @Test
    fun `Given a valid request, should be done to correct url`() =
        testCoroutineExtension.runBlockingTest {

            // GIVEN
            enqueueResponse(HttpURLConnection.HTTP_OK)
            println("⏰ First job ${Thread.currentThread().name}")

            println("⏰ Second job START in thread: ${Thread.currentThread().name}")
            launch(this.coroutineContext) {
                api.getPosts()
            }
            advanceUntilIdle()
            println("⏰ Second job END in thread: ${Thread.currentThread().name}")

//            launch(testCoroutineScope.coroutineContext) {
            val request = mockWebServer.takeRequest()

            // THEN
            println("🎃 THEN in thread: ${Thread.currentThread().name}")
            Truth.assertThat(request.path).isEqualTo("/posts")
        }

    /**
     * ❌ This test FAILS, with launch builder sometimes fail, sometimes PASS
     */
    @Test
    fun `Given api returns 200, should have list of posts`() =
        testCoroutineExtension.runBlockingTest {

            // GIVEN
            enqueueResponse(HttpURLConnection.HTTP_OK)
            println("⏰ First job ${Thread.currentThread().name}")

            // WHEN
            var postList: List<PostDTO> = emptyList()

            println("⏰ Second job START in thread: ${Thread.currentThread().name}")
            launch(this.coroutineContext) {
                postList = api.getPosts()
            }
            advanceUntilIdle()
            println("⏰ Second job END in thread: ${Thread.currentThread().name}")

            // THEN
            println("🎃 THEN in thread: ${Thread.currentThread().name}")
            Truth.assertThat(postList).isNotNull()
            Truth.assertThat(postList?.size).isEqualTo(100)
        }

    @BeforeEach
    override fun setUp() {
        super.setUp()

        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PostApi::class.java)
    }

    @AfterEach
    override fun tearDown() {
        super.tearDown()
    }
}
