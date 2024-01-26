plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.gifs_watcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gifs_watcher"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        setProperty("archivesBaseName", "GifsWatcher-v$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures {
            buildConfig = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isDebuggable = true
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("develop") {
            dimension = "environment"
            versionNameSuffix = "-dev"
            buildConfigField("String", "TENOR_API_KEY", "\"AIzaSyB-2TBQ-nEu3fCtMpo9EN9mfqjb9P9A3OA\"")
        }

        create("production") {
            dimension = "environment"
            versionNameSuffix = "-prod"
            buildConfigField("String", "TENOR_API_KEY", "\"AIzaSyB-2TBQ-nEu3fCtMpo9EN9mfqjb9P9A3OA\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.3")

    // Gson
    implementation ("com.google.code.gson:gson:2.8.7")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Picasso
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("jp.wasabeef:picasso-transformations:2.4.0")

    // Co Routine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // Chiffrement
    implementation("com.ToxicBakery.library.bcrypt:bcrypt:1.0.9")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("jp.wasabeef:glide-transformations:4.3.0")
    implementation ("jp.co.cyberagent.android:gpuimage:2.1.0")

    // QR Code
    implementation("io.github.g0dkar:qrcode-kotlin-android:3.3.0")
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    // Scan QR Code
    implementation("com.google.android.gms:play-services-vision:20.1.3")

    // CameraX library
    implementation("androidx.camera:camera-camera2:1.0.1")
    implementation("androidx.camera:camera-lifecycle:1.0.1")
    implementation("androidx.camera:camera-view:1.0.0-alpha30")

    // Easy Permissions
    implementation("pub.devrel:easypermissions:3.0.0")

    // Animations
    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation("com.daimajia.androidanimations:library:2.3@aar")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}