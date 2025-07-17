import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    tasks.create("testClasses")

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(libs.material.icons.extended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.kotlin.serialization)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.calf.file.picker)
            implementation(libs.calf.file.picker.coil)
            implementation(libs.coil.compose.v300alpha8)
            implementation(libs.peekaboo.ui)
            implementation(libs.peekaboo.image.picker)
            implementation(libs.filekit.core)
            implementation(libs.filekit.compose)
            implementation(libs.moko.mvvm)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.kevinnzou.webview)
            implementation(libs.moko.biometry)
            implementation(libs.moko.biometry.compose)
            implementation(libs.composeIcons.tablerIcons)
            implementation(libs.composeIcons.evaIcons)
            implementation(libs.konnectivity)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "org.itb.sga"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.itb.sga"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 17
        versionName = "1.0.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.androidx.lifecycle.livedata.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.core)
    implementation(libs.places)
    implementation(libs.protolite.well.known.types)
    implementation(libs.androidx.core.i18n)
    implementation(libs.androidx.appcompat)
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.7.0")
    }
}