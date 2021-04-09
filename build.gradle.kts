plugins {
    antlr
    java
    application
    kotlin("jvm") version "1.4.32"
}

group = "com.artbobrov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    antlr("org.antlr", "antlr4", "4.8")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.7.0")
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
