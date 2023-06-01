plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val composeVersion = "1.1.1"

android {
    defaultConfig {
        minSdk = 26
        compileSdk = 31
        targetSdk = 30
        applicationId = "de.eso.weather.compose"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["coverage"] = "true"

        buildFeatures.apply {
            viewBinding = true
            compose = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        composeOptions {
            kotlinCompilerExtensionVersion = composeVersion
        }

        kotlinOptions {
            jvmTarget = "11"
        }

        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
            animationsDisabled = true
            unitTests.isReturnDefaultValues = true
        }

        buildTypes {
            getByName("debug") {
                isTestCoverageEnabled = true
            }

            getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    namespace = "de.eso.weather"
    testNamespace = "de.eso.weather.test"
}

val lifecycleVersion = "2.4.1"
val navigationVersion = "2.4.2"

val koinVersion = "2.2.3"
val moshiVersion = "1.13.0"

val mockkVersion = "1.12.0"
val jupiterVersion = "5.7.2"
val assertjVersion = "3.18.1"

// Main Dependencies
dependencies {

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.core:core-ktx:1.7.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion")

    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // Jetpack Compose
    implementation("androidx.compose.compiler:compiler:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    // Material Design
    implementation("androidx.compose.material:material:$composeVersion")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.runtime:runtime-rxjava3:$composeVersion")

    implementation("androidx.datastore:datastore-preferences-rxjava3:1.0.0")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")

    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-scope:$koinVersion")
    implementation("io.insert-koin:koin-androidx-viewmodel:$koinVersion")
    implementation("io.insert-koin:koin-androidx-fragment:$koinVersion")

    implementation("com.google.android.material:material:1.5.0")

    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha02")

    implementation("androidx.navigation:navigation-compose:2.5.0-rc02")

    implementation("com.squareup.moshi:moshi:$moshiVersion")
    implementation("com.squareup.moshi:moshi-adapters:$moshiVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
}

// Unit Test Dependencies
dependencies {
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")

    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
}

// androidTest Dependencies
dependencies {
    androidTestImplementation("androidx.navigation:navigation-testing:$navigationVersion")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("org.assertj:assertj-core:$assertjVersion")

    androidTestImplementation("io.insert-koin:koin-test:$koinVersion")
    debugImplementation("androidx.fragment:fragment-testing:1.4.1") {
        exclude("androidx.test", "core")
    }

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    // 1.4.0 on purpose, because Google has not released v1.4.1 of the runner, yet.
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestUtil("androidx.test:orchestrator:1.4.1")
}
