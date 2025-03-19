package org.itb.sga.core

import platform.Foundation.NSLog

actual fun logInfo(tag: String, message: String) {
    NSLog("$tag: $message")
}