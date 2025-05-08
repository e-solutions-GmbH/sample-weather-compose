plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    defaultConfig {
        minSdk = 26
        compileSdk = 34
        targetSdk = 34
        applicationId = "de.eso.weather.compose"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["coverage"] = "true"

        buildFeatures.apply {
            viewBinding = true
            compose = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
        }

        kotlinOptions {
            jvmTarget = "17"
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

    namespace = "de.eso.weather"
    testNamespace = "de.eso.weather.test"
}

// Main Dependencies
dependencies {
    implementation(libs.appcompat)
    implementation(libs.coreKtx)

    implementation(libs.lifecycleViewModelKtx)
    implementation(libs.lifecycleLiveDataKtx)
    implementation(libs.lifecycleReactiveStreamsKtx)

    implementation(libs.navigationFragmentKtx)
    implementation(libs.navigationUiKtx)

    // Jetpack Compose
    implementation(platform(libs.composeBom))

    implementation(libs.composeCompiler)
    implementation(libs.composeUi)
    implementation(libs.composeUiTooling)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterialIconsCore)
    implementation(libs.composeMaterialIconsExtended)
    implementation(libs.composeRuntimeLiveData)
    implementation(libs.composeRuntimeRxJava3)
    implementation(libs.constraintlayoutCompose)
    implementation(libs.navigationCompose)

    implementation(libs.datastorePreferencesRxJava3)

    implementation(libs.koinAndroid)
    implementation(libs.koinAndroidScope)
    implementation(libs.koinAndroidViewModel)
    implementation(libs.koinAndroidFragment)

    implementation(libs.rxKotlin)
    implementation(libs.rxAndroid)

    implementation(libs.moshi)
    implementation(libs.moshiAdapters)
    implementation(libs.moshiKotlin)
}

// Unit Test Dependencies
dependencies {
    testImplementation(libs.coreTesting)
    testImplementation(libs.assertjCore)
    testImplementation(libs.mockk)

    testImplementation(libs.koinTest)
    testImplementation(libs.koinTestJunit5)

    testImplementation(libs.junitJupiterApi)
    testRuntimeOnly(libs.junitJupiterEngine)
    testImplementation(libs.junitJupiterParams)
}

// androidTest Dependencies
dependencies {
    androidTestImplementation(libs.androidTestCore)
    androidTestImplementation(libs.espressoCore)
    androidTestImplementation(libs.espressoIntents)
    androidTestImplementation(libs.androidTestExtJunitKtx)
    androidTestImplementation(libs.mockkAndroid)
    androidTestImplementation(libs.assertjCore)

    androidTestImplementation(libs.koinTest)

    androidTestImplementation(libs.androidTestRunner)
    androidTestUtil(libs.androidTestOrchestrator)

    // Compose
    androidTestImplementation(platform(libs.composeBom))
    androidTestImplementation(libs.composeUiTestJunit4)
    debugImplementation(libs.composeUiTestManifest)
}
