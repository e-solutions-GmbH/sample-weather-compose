import org.gradle.api.tasks.testing.logging.TestLogEvent
import pl.droidsonroids.gradle.pitest.PitestPlugin.PITEST_CONFIGURATION_NAME
import pl.droidsonroids.gradle.pitest.PitestPluginExtension

plugins {
    id("com.android.application") version "8.4.2" apply false
    id("com.android.library") version "8.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("pl.droidsonroids.pitest") version "0.2.18" apply false
}

buildscript {
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.3")
    }
}

subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
    }

    apply(plugin = "pl.droidsonroids.pitest")

    buildscript {
        dependencies.add(
            PITEST_CONFIGURATION_NAME,
            "com.groupcdg.pitest:pitest-kotlin-plugin:1.3.0"
        )
        dependencies.add(
            PITEST_CONFIGURATION_NAME,
            "com.groupcdg.pitest:pitest-accelerator-junit5:1.2.0"
        )
        dependencies.add(PITEST_CONFIGURATION_NAME, "com.arcmutate:android:0.0.1")
        dependencies.add(PITEST_CONFIGURATION_NAME, "com.groupcdg.arcmutate:base:1.3.1")
        dependencies.add(PITEST_CONFIGURATION_NAME, "com.groupcdg:pitest-git-plugin:1.3.3")
    }

    extensions.findByType<PitestPluginExtension>()?.apply {
        setOf("de.eso.*").also { targets ->
            targetClasses.set(targets)
            targetTests.set(targets.map { "${it}Test" })
        }
        junit5PluginVersion.set("1.0.0")
        pitestVersion.set("1.17.0")
        mutators.set(setOf("STRONGER", "EXTENDED"))
    }
}
