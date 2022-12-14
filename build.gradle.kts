
plugins {
    id("java")
    id("com.diffplug.spotless") version "6.11.0"
    id("com.coditory.integration-test") version "1.4.4"
    id("io.quarkus")
    id("com.palantir.docker") version "0.34.0"
}

group = "hu.blzsaa"
version = "1.1.1"

repositories {
    mavenCentral()
}

val invoker = configurations.create("invoker")

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-hibernate-validator")

    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("io.quarkus:quarkus-google-cloud-functions")
    implementation("io.quarkus:quarkus-arc")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    testImplementation("io.quarkus:quarkus-junit5")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.mockito:mockito-core")

    integrationImplementation("org.testcontainers:testcontainers")
    integrationImplementation("org.testcontainers:junit-jupiter")
    integrationImplementation("io.rest-assured:rest-assured")

    invoker("com.google.cloud.functions.invoker:java-function-invoker:1.1.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    java {
        googleJavaFormat()
    }
    kotlinGradle {
        ktlint()
    }
}

docker {
    name = "wy-space"
    copySpec.from(configurations["invoker"].files.iterator().next().absolutePath).into("/home")
    copySpec.from("build/wy-space-${project.version}-runner.jar").into("/home")
}

tasks["dockerPrepare"].dependsOn("quarkusBuild")
tasks["integrationTest"].dependsOn("docker")
