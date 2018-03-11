package com.anjeludo.kotlinwebfluxexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinWebfluxExampleApplication

fun main(args: Array<String>) {
    runApplication<KotlinWebfluxExampleApplication>(*args)
}

data class Comment(
        val postId: Int,
        val id: Int,
        val name: String,
        val email: String,
        val body: String
)

data class Post(
        val userId: Int,
        val id: Int,
        val title: String,
        val body: String
)

data class Response(
        val postId: Int,
        val userId: Int,
        val title: String,
        val comments: List<LightComment>
)

data class LightComment(
        val email: String,
        val body: String
)

