package org.itb.sga.core


class IOSPlatform: Platform {
//    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val name: String = "iOS"
}

actual fun getPlatform(): Platform = IOSPlatform()