package com.example.data.remote.mapper

import com.example.data.local.entity.FavoritePost
import com.example.data.local.entity.LocalPost
import com.example.data.remote.dto.PostDto
import com.example.domain.entity.Post

fun PostDto.toEntity(): Post {
    return Post(
        userId = userId,
        id = id,
        title = title,
        body = body
    )
}


fun Post.toLocal(): LocalPost {
    return LocalPost(
        userId = userId,
        id = id,
        title = title,
        body = body
    )
}

fun Post.toFavorite(): FavoritePost {
    return FavoritePost(
        userId = userId,
        id = id,
        title = title,
        body = body
    )
}

fun FavoritePost.toEntity(): Post {
    return Post(
        userId = userId,
        id = id,
        title = title,
        body = body
    )
}

fun List<FavoritePost>.favoritesToEntityList() = map { it.toEntity() }

fun List<PostDto>.toEntityList() = this.map { it.toEntity() }


fun LocalPost.toEntity(): Post {
    return Post(
        userId = userId,
        id = id,
        title = title,
        body = body
    )
}

fun List<Post>.toLocal() = map { it.toLocal() }
fun List<LocalPost>.toEntityPostsFromLocal() = map { it.toEntity() }