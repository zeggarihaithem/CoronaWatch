package com.example.coronawatch_mobile.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

import solutus.coronawatch.data.db.entity.Post
import solutus.coronawatch.data.network.implementation.ContentApi
import solutus.coronawatch.data.reposetory.implementation.ContentRepository
import kotlin.collections.ArrayList

class ContentRepositoryTest {

    private val contentRepository =
        ContentRepository(
            ContentApi.invoke()
        )


    @Test
    fun getPosts() = runBlocking {
        val posts = contentRepository.getPosts()
        println(posts?.get(1)?.title.toString())
        Assert.assertEquals(posts?.get(0)?.title.toString() , "n'importe quoi")
    }

    @Test
    fun getVideos() = runBlocking {
        val posts:ArrayList<Post> = contentRepository.getPosts()!!
        for (post in posts){
            println(post.content)
        }
        val videos = contentRepository.createVideos(posts)
        for (video in videos){
            println(video.url)
        }
        print(!videos.isEmpty())
    }
}