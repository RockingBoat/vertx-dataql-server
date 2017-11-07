//import org.gradle.script.lang.kotlin.repositories
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask

import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension
import org.jetbrains.kotlin.gradle.dsl.Coroutines

project.group = "rockingboat.vertx.dataql.server"
project.version = "0.1-SNAPSHOT"

project.defaultTasks("run")


//mainClassName = "io.vertx.core.Launcher"
val mainVerticleName = "rockingboat.vertx.dataql.server.App"




buildscript {
    var kotlinVersion: String by extra

    kotlinVersion = "1.1.50"



    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:latest.release")
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:latest.release")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.github.jengelman.gradle.plugins:shadow:2.0.0")
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0-M4")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.15")

    }
}

plugins {
    application
    java
}

apply {
    plugin("kotlin")
    plugin("org.junit.platform.gradle.plugin")
}


repositories {
    mavenCentral()
    jcenter()
    maven { setUrl("https://dl.bintray.com/rockingboat/maven") }
}


configure<JavaPluginConvention> {
    setSourceCompatibility(1.8)
    setTargetCompatibility(1.8)
}

java {

    sourceSets {
        "main" {
            java {
                srcDir(files("src"))
            }
        }
        "test" {
            java {
                srcDir(files("tests"))
            }
        }
    }
}

application {
    mainClassName = "io.vertx.core.Launcher"
}




dependencies {
    val kotlinVersion: String by extra
    var vertxVersion: String by extra
    var kodeinVersion: String by extra
    var kotlinCoroutinsVersion: String by extra

    vertxVersion = "3.4.2"
    kodeinVersion = "4.1.0"
    kotlinCoroutinsVersion = "0.18"


    compile("io.vertx:vertx-core:$vertxVersion")
    compile("io.vertx:vertx-lang-kotlin:$vertxVersion")
    compile("io.vertx:vertx-web:$vertxVersion")

    compile("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinsVersion")

    compile("com.github.salomonbrys.kodein:kodein:$kodeinVersion")
    compile("io.github.jupf.staticlog:staticlog:2.1.9")
    compile("org.slf4j:slf4j-api:1.7.25")
    compile("org.slf4j:slf4j-simple:1.7.25")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0")
    compile("rockingboat.vertx.helpers:web:0.8.2")

}

tasks.withType<Wrapper> {
    gradleVersion = "4.2"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


//tasks.withType<DokkaTask>() {
//    outputFormat = "kotlin-website"
//    outputDirectory = "${buildDir}/kws"
//}
//
//
//val shadowJar: ShadowJar by tasks
//shadowJar.apply {
//    classifier = "fat"
//    manifest.attributes.apply {
//        put("Main-Verticle", mainVerticleName)
//        put("Main-Class", application.mainClassName)
//    }
//
//    mergeServiceFiles {
//        include("META-INF/services/io.vertx.core.spi.VerticleFactory")
//    }
//
//}
//

fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
        else              -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}

fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
        else              -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}

//kotlin {
//    experimental.coroutines = Coroutines.ENABLE
//}