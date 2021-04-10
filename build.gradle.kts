plugins {
    antlr
    java
    application
    kotlin("jvm") version "1.4.32"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

group = "com.artbobrov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("MainKt")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    antlr("org.antlr", "antlr4", "4.8")
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.7.0")
}

ktlint {
    enableExperimentalRules.set(true)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    generateGrammarSource {
        outputs.cacheIf { true }
        inputs.dir("src/main/antlr")
        outputDirectory = File("src/main/java/antlr")
        arguments = arguments + listOf("-visitor", "-long-messages", "-package", "antlr")
    }

    test {
        useJUnitPlatform()
    }

    compileKotlin {
        dependsOn(generateGrammarSource)
    }
}
