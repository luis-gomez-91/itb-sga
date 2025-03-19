package org.itb.sga.core

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class PDFOpenerIOS : URLOpener {
    override fun openURL(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }
}