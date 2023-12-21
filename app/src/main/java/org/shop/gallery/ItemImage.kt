package org.shop.gallery

import android.net.Uri

sealed class ImageItems {
    data class Image(
        val uri: Uri,
    ) : ImageItems()

    object LoadMore : ImageItems()
}