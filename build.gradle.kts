
plugins {
    id("java")
    id("com.diffplug.spotless") version "6.11.0"
    id("com.coditory.integration-test") version "1.4.4"
    id("io.quarkus")
    id("com.palantir.docker") version "0.34.0"
}

group = "hu.blzsaa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val invoker = configurations.create("invoker")

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("net.sf.supercsv:super-csv:2.4.0")

    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("io.quarkus:quarkus-google-cloud-functions")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("com.google.code.bean-matchers:bean-matchers:0.14")

    integrationImplementation("org.testcontainers:testcontainers:1.17.4")
    integrationImplementation("org.testcontainers:junit-jupiter:1.17.4")
    integrationImplementation("io.rest-assured:rest-assured:5.2.0")

    invoker("com.google.cloud.functions.invoker:java-function-invoker:1.1.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
    copySpec.from("build/wy-space-1.0-SNAPSHOT-runner.jar").into("/home")
}

tasks["dockerPrepare"].dependsOn("quarkusBuild")
tasks["integrationTest"].dependsOn("docker")
