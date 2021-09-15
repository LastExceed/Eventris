import org.gradle.internal.os.OperatingSystem

plugins {
	kotlin("jvm") version "1.5.0"
}

repositories {
	maven("https://maven.pkg.github.com/Dominaezzz/matrix-kt") {
		credentials {
			username = System.getenv("GITHUB_USER") // Your GitHub username
			password = System.getenv("GITHUB_TOKEN") // A GitHub token with `read:packages`
		}
	}
	mavenCentral()
}

val host: OperatingSystem = OperatingSystem.current()

val lwjglNatives = when {
	host.isLinux -> "natives-linux"
	host.isMacOsX -> "natives-macos"
	host.isWindows -> "natives-windows"
	else -> error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

dependencies {
	setOf(
		"glfw",
		"opengl"
	).forEach { implementation("com.kgl", "kgl-$it", "0.1.11") }

	setOf(
		"",
		"-glfw",
		"-opengl"
	).forEach { runtimeOnly("org.lwjgl", "lwjgl$it", "3.2.3", classifier = lwjglNatives) }
}