plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

android {
    namespace = "de.eso.weather.domain"
    testNamespace = "de.eso.weather.test"
    defaultConfig {
        minSdk = 26
        compileSdk = 34
        targetSdk = 34
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true
        unitTests.isReturnDefaultValues = true
    }
}

// Main Dependencies
dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.reactivestreams.ktx)
    implementation(libs.androidx.lifecycle.reactivestreams.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.compiler)
    implementation(libs.androidx.compose.ui)
    // Tooling support (Previews, etc.)
    implementation(libs.androidx.compose.ui.tooling)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(libs.androidx.compose.foundation)
    // Material Design
    implementation(libs.androidx.compose.material)
    // Material design icons
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    // Integration with observables
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.runtime.rxjava3)
    // ConstraintLayout
    implementation(libs.androidx.constraintlayout.compose)
    // Navigation
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.datastore.preferences.rxjava3)

    implementation(libs.koin.android)

    implementation(libs.rxjava3.rxkotlin)
    implementation(libs.rxjava3.rxandroid)

    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.kotlin)
}

// Unit Test Dependencies
dependencies {
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.assertj.core)
    testImplementation(libs.mockk)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
}

// Maven Publishing Configuration
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "de.eso.weather"
            artifactId = "someLib"
            version = "1.0.0"
            
            afterEvaluate {
                from(components["release"])
            }
        }
    }
    
    repositories {
        maven {
            name = "localRepository"
            url = uri("${rootProject.projectDir}/local-repo")
        }
    }
}
