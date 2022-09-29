plugins {
    id("java")
    id("com.diffplug.spotless") version "6.11.0"
    id("com.coditory.integration-test") version "1.4.4"
}

group = "hu.blzsaa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.sf.supercsv:super-csv:2.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("com.google.code.bean-matchers:bean-matchers:0.14")

    // transitive vulnerable dependency commons-io:commons-io:2.6 CVE-2021-29425
    integrationImplementation("org.quickperf:quick-perf-junit5:1.1.0") {
        exclude(group = "commons-io", module = "commons-io")
    }
    integrationImplementation("commons-io:commons-io:2.11.0")
    integrationImplementation("org.junit.platform:junit-platform-launcher:1.9.1")
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
