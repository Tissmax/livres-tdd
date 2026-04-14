plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
    id ("java")
    id ("info.solidsoft.pitest") version "1.19.0"
    jacoco

}

group = "com.mathis"
version = "0.0.1-SNAPSHOT"
description = "livres"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("io.kotest:kotest-property:5.9.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.mockk:mockk:1.14.9")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("kotest.framework.classpath.scanning.autoscan.disable", "true")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

pitest {
    // Cible spécifiquement votre package
    targetClasses.set(listOf("com.mathis.*"))

    // Utilise l'exécuteur JUnit5 (compatible avec Kotest)
    testPlugin.set("junit5")

    // Génère un rapport HTML détaillé
    outputFormats.set(listOf("HTML", "XML"))

    // Mutation engine pour Kotlin
    pitestVersion.set("1.16.0")
}