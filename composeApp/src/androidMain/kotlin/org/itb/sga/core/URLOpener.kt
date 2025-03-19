package org.itb.sga.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class URLOpenerAndroid(private val context: Context) : URLOpener {
    override fun openURL(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Error al intentar abrir la URL: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

