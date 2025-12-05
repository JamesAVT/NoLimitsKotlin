// Ruta: app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt") // Necesario para Room (anotaciones -> código generado)
}

android {
    namespace = "com.example.nolimits"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.nolimits"

        minSdk = 30
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        // Runner para tests instrumentados (AndroidTest)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    // -----------------------------
    // Navegación y ViewModels (Compose)
    // -----------------------------
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0-rc02")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // -----------------------------
    // DataStore
    // -----------------------------
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // -----------------------------
    // Room
    // -----------------------------
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // -----------------------------
    // Compose (sin BOM)
    // -----------------------------
    implementation("androidx.compose.ui:ui:1.7.2")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.2")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.2")

    // -----------------------------
    // Retrofit + OkHttp
    // -----------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // -----------------------------
    // Coil
    // -----------------------------
    implementation("io.coil-kt:coil-compose:2.3.0")

    // -----------------------------
    // Material Icons
    // -----------------------------
    implementation("androidx.compose.material:material-icons-extended")

    // -----------------------------
    // AndroidX base (Version Catalog)
    // -----------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)

    // -----------------------------
    // TESTS UNITARIOS (local unit tests)
    // -----------------------------

    // JUnit 4 clásico (para tests locales normales)
    testImplementation("junit:junit:4.13.2")

    // Kotest + JUnit 5 (si más adelante quieres usar JUnit 5)
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    // Mockk
    testImplementation("io.mockk:mockk:1.13.10")

    // -----------------------------
    // TESTS INSTRUMENTADOS (AndroidTest)
    // -----------------------------

    // JUnit para AndroidX
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // Espresso actualizado (evita el crash de InputManager en Android 14+)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose UI tests (JUnit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Herramientas de depuración / manifest de tests
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// ------------------------------------
// Configuración global SOLO para tests locales (JUnit5)
// ------------------------------------
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}