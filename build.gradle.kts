import org.gradle.script.lang.kotlin.repositories
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask

import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

project.group = "backend.mobile.apiGateway"
project.version = "1.2-SNAPSHOT"

project.defaultTasks("run")


//mainClassName = "io.vertx.core.Launcher"
val mainVerticleName = "backend.mobile.apiGateway.App"




buildscript {
    val kotlinVersion = "1.1.4-3"

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
    id("io.gitlab.arturbosch.detekt").version("1.0.0.M12.3")
}

apply {
    plugin("kotlin")
    plugin("com.github.johnrengelman.shadow")
    plugin("io.gitlab.arturbosch.detekt")
    plugin("org.junit.platform.gradle.plugin")
    plugin("org.jetbrains.dokka")
}

//configure {
//    filters {
//        engines {
//            include("spek")
//        }
//    }
//}


repositories {
    mavenCentral()
    jcenter()
    flatDir {
        dirs("external")
    }
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
    val kotlinVersion = "1.1.4-3"
    val vertxVersion = "3.4.1"

    compile("io.vertx:vertx-core:$vertxVersion")
    compile("io.vertx:vertx-lang-kotlin:$vertxVersion")
    compile("io.vertx:vertx-web:$vertxVersion")

    compile("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")


//    testCompile("ch.qos.logback:logback-classic:1.2.3")
//
//    testCompile("org.jetbrains.spek:spek-api:1.1.2")
//    testCompile("com.natpryce:hamkrest:1.4.1.0")
//    testCompile("org.junit.platform:junit-platform-launcher:1.0.0-M4")
//
//
//    testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.2")
}

tasks.withType<Wrapper> {
    gradleVersion = "4.0"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


tasks.withType<DokkaTask>() {
    outputFormat = "kotlin-website"
    outputDirectory = "${buildDir}/kws"
}


val shadowJar: ShadowJar by tasks
shadowJar.apply {
    classifier = "fat"
    manifest.attributes.apply {
        put("Main-Verticle", mainVerticleName)
        put("Main-Class", application.mainClassName)
    }

    mergeServiceFiles {
        include("META-INF/services/io.vertx.core.spi.VerticleFactory")
    }

}


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