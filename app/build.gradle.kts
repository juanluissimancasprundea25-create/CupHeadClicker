plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.juanluis.cupheadclicker"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.juanluis.cupheadclicker"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Room: base de datos local SQLite (Tarea 2 y 3)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // ViewModel y LiveData (arquitectura MVVM)
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.7")

    // Lista del ranking
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // LeakCanary: detecta fugas de memoria SOLO en la versión debug
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}