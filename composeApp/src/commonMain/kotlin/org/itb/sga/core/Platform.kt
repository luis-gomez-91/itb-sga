package org.itb.sga.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform