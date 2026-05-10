plugins {
    kotlin("jvm") version "2.2.21"
    antlr
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(23)
}

application {
    mainClass.set("org.example.MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}


tasks.test {
    useJUnitPlatform()
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor", "-listener")
}

tasks.generateTestGrammarSource {
    arguments = arguments + listOf("-visitor", "-listener")
}

tasks.compileKotlin {
    dependsOn(tasks.generateGrammarSource)
}

tasks.compileTestKotlin {
    dependsOn(tasks.generateTestGrammarSource)
}