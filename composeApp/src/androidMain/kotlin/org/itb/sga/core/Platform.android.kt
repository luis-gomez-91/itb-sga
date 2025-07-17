package org.itb.sga.core


class AndroidPlatform : Platform {
    override val name: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()