package com.keelim.core.database.mapper

import com.keelim.core.database.model.DeepLinkEntity
import com.keelim.model.DeepLink

fun DeepLinkEntity.toDeepLink() = DeepLink(
    url = url,
    timestamp = timestamp,
    isBookMarked = isBookMarked,
    title = title,
)

fun List<DeepLinkEntity>.toDeepLink() = map(DeepLinkEntity::toDeepLink)

fun DeepLink.toDeepLinkEntity() = DeepLinkEntity(
    url = url,
    timestamp = timestamp,
    isBookMarked = isBookMarked,
    title = title,
)
