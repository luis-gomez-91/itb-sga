package org.example.aok.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

//class PDFOpenerAndroid(private val context: Context) : PDFOpener {
//    override fun openPDF(url: String) {
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse(url)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        if (intent.resolveActivity(context.packageManager) != null) {
//            context.startActivity(intent)
//        } else {
//            // Manejo en caso de que no haya navegador disponible
//        }
//    }
//}
class PDFOpenerAndroid(private val context: Context) : PDFOpener {
    override fun openPDF(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        // Verificamos si hay aplicaciones que pueden manejar el Intent
        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)

        if (activities.isNotEmpty()) {
            context.startActivity(intent)
        } else {
            // Si no hay aplicaciones disponibles, mostramos un mensaje
            Toast.makeText(context, "No hay aplicaciones disponibles para abrir el PDF", Toast.LENGTH_SHORT).show()
        }
    }
}


