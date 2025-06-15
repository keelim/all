package com.keelim.core.database.mapper

import com.keelim.model.DeepLink
import com.keelim.shared.data.database.model.DeepLinkEntity

fun DeepLinkEntity.toDeepLink() = DeepLink(
    url = url,
    timestamp = timestamp,
    isBookMarked = isBookMarked,
    title = title,
    category = category,
)

fun List<DeepLinkEntity>.toDeepLink() = map(DeepLinkEntity::toDeepLink)

fun DeepLink.toDeepLinkEntity() = DeepLinkEntity(
    url = url,
    timestamp = timestamp,
    isBookMarked = isBookMarked,
    title = title,
    category = category,
)
