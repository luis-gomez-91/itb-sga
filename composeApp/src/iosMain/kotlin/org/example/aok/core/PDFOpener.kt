package org.example.aok.core

import platform.Foundation.NSURL
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIViewController
import platform.UIKit.UIApplication

class PDFOpenerIOS : PDFOpener {
    override fun openPDF(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }
}