import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    id("com.vanniktech.maven.publish") version "0.36.0"
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        publishLibraryVariants("release", "debug")
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "messagebar"
            isStatic = true
            linkerOpts.add("-Xbinary=bundleId=com.stevdza_san.messagebar")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        iosMain.dependencies {}
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)

            implementation(libs.compose.icons)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.stevdza_san.messagebarkmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

mavenPublishing {
    coordinates(
        groupId = "com.stevdza-san",
        artifactId = "messagebarkmp",
        version = "1.0.10"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Message Bar KMP")
        description.set("Animated Message Bar UI that can be wrapped around your composable content in order to display Error/Success messages in your app. Adapted to Material 3.")
        inceptionYear.set("2024")
        url.set("https://github.com/stevdza-san/MessageBar-KMP")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("stevdza-san")
                name.set("Stefan Jovanovic")
                email.set("stefan.jovanavich@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/stevdza-san/MessageBar-KMP")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral()

    // Enable GPG signing for all publications if keys are present
    if (project.hasProperty("signing.keyId")) {
        signAllPublications()
    }
}