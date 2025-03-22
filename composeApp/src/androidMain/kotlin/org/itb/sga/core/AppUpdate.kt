package org.itb.sga.core

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import org.itb.sga.App

actual fun appIsLastVersion(lastVersion: Int): Boolean {
    val context = App.context
    val currentVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionCode

    return currentVersion >= lastVersion
}

actual fun openPlayStoreOrAppStore() {
    val context = App.context // Aquí obtenemos el contexto de la clase `App`
    val appPackageName = context.packageName
    val playStoreUrl = "market://details?id=$appPackageName"
    val webUrl = "https://play.google.com/store/apps/details?id=$appPackageName"

    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))

        // Si el contexto no es una actividad, debemos añadir la bandera FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    } catch (e: android.content.ActivityNotFoundException) {
        // Si no se puede abrir la Google Play Store, abrir la URL web
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
            // También añades la bandera para evitar el error en contextos no relacionados con Activity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (innerException: Exception) {
            Toast.makeText(context, "No se puede abrir la tienda", Toast.LENGTH_SHORT).show()
        }
    }
}