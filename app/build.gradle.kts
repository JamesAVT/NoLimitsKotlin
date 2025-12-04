plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt") // Necesario para Room (anotaciones -> código generado)
}

android {
    namespace = "com.example.nolimits"

    // Versión del SDK con la que compilas la app
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.nolimits"

        // Mínima versión de Android soportada
        minSdk = 30

        // Versión objetivo (optimización / comportamiento)
        targetSdk = 36

        // Versión interna y visible de la app
        versionCode = 1
        versionName = "1.0"

        // Runner para tests instrumentados (AndroidTest)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Minify (ProGuard/R8) desactivado en debug
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Compatibilidad con Java
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Target de JVM para Kotlin
    kotlinOptions {
        jvmTarget = "11"
    }

    // Activar Jetpack Compose
    buildFeatures {
        compose = true
    }
}

/**
 * Configuración de Kapt (procesador de anotaciones de Kotlin).
 *
 * - correctErrorTypes = true:
 *   Hace que Kapt tolere anotaciones que aún no puede resolver bien
 *   durante la generación de stubs Java, evitando errores como
 *   "NonExistentClass cannot be converted to Annotation".
 */
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

    // Corrutinas para operaciones asíncronas (llamadas a API, etc.)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // -----------------------------
    // DataStore (preferencias)
    // -----------------------------
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // -----------------------------
    // Room (base de datos local)
    // -----------------------------
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // -----------------------------
    // Compose "suelto" (sin BOM)
    // (OJO: podrías limpiarlo luego y quedarte solo con el BOM)
    // -----------------------------
    implementation("androidx.compose.ui:ui:1.7.2")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.2")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.2")

    // -----------------------------
    // Retrofit + OkHttp (consumo de APIs)
    // -----------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // -----------------------------
    // Coil (carga de imágenes en Compose)
    // -----------------------------
    implementation("io.coil-kt:coil-compose:2.3.0")

    // -----------------------------
    // Icons Material para Compose
    // -----------------------------
    implementation("androidx.compose.material:material-icons-extended")

    // -----------------------------
    // AndroidX base (usando tu Version Catalog)
    // -----------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // BOM de Compose (centraliza versiones de Compose declaradas con libs.androidx...)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)

    // -----------------------------
    // TESTS UNITARIOS (JUnit 5 + Kotest + Mockk)
    // -----------------------------

    // ELIMINADO: testImplementation(libs.junit)  // JUnit4 se reemplaza por JUnit 5

    // Kotest: runner y assertions (estilo más cómodo para tests)
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // JUnit 5 (plataforma estándar)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    // Mockk para mocks en Kotlin
    testImplementation("io.mockk:mockk:1.13.10")

    // -----------------------------
    // TESTS INSTRUMENTADOS (AndroidTest)
    // -----------------------------
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Herramientas de depuración de Compose
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// ------------------------------------
// Configuración global de tests (JUnit 5)
// ------------------------------------
tasks.withType<Test>().configureEach {
    // Obliga a Gradle a usar la plataforma de JUnit 5
    useJUnitPlatform()
}