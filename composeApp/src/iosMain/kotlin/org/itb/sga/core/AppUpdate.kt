package org.itb.sga.core

import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun appIsLastVersion(lastVersion: Int): Boolean {
    // Obtén la versión actual de la app en iOS
    val currentVersion = (NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String)?.toIntOrNull() ?: 0
    return currentVersion >= lastVersion
}

actual fun openPlayStoreOrAppStore() {
    // En iOS, redirigimos a la App Store
    val appStoreUrl = "https://apps.apple.com/ec/app/itb-sga/id6748590300"
    UIApplication.sharedApplication.openURL(NSURL(string = appStoreUrl))
}