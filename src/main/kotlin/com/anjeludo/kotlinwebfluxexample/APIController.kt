package com.anjeludo.kotlinwebfluxexample

import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.core.scheduler.Schedulers

@Controller
@RequestMapping(path = ["/api"], produces = [ APPLICATION_JSON_UTF8_VALUE ])
class APIController(private val aPIService: APIService) {

    @RequestMapping(method = [RequestMethod.GET])
    fun getData(): Mono<ResponseEntity<List<Response>>> {

        return aPIService.fetchPosts()
                .filter { it -> it.userId % 2 == 0} // id par
                .take(20) // 20 primeros
                .parallel(4)
                .runOn(Schedulers.parallel())
                .map { post -> aPIService.fetchComments(post.id)
                        .map { comment -> LightComment(email = comment.email, body = comment.body) }
                        .collectList()
                        .zipWith(post.toMono()) }
                .flatMap { it -> it }
                .map { result -> Response(
                        postId = result.t2.id,
                        userId = result.t2.userId,
                        title =  result.t2.title,
                        comments = result.t1
                ) }
                .sequential()
                .collectList()
                .map { body -> ResponseEntity.ok().body(body) }
                .toMono()

    /*    return apiService.fetchPosts()
                .filter { it -> it.userId % 2 == 0 }
                .take(20)
                .parallel(4)
                .runOn(Schedulers.parallel())
                .map { post -> apiService.fetchComments(post.id)
                        .map { comment -> LightComment(email = comment.email, body = comment.body) }
                        .collectList()
                        .zipWith(post.toMono()) }

                .flatMap { it -> it }
                .map { result -> Response(
                        postId = result.t2.id,
                        userId = result.t2.userId,
                        title = result.t2.title,
                        comments = result.t1
                ) }
                .sequential()
                .collectList()
                .map { body -> ResponseEntity.ok().body(body) }
                .toMono()
                */

    }
}