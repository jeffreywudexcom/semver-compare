import org.apache.maven.artifact.versioning.ComparableVersion
import java.util.regex.Pattern

fun isSemanticVersionUsingComparableVersion(version: String): Boolean {
    return try {
        ComparableVersion(version)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun isSemanticVersionUsingRegex(version: String?): Boolean {
    val semverRegex =
        "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)" +
        "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)" +
        "(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" +
        "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$"
    val semverPattern = Pattern.compile(semverRegex)

    if (version == null) {
        return false
    }
    val matcher = semverPattern.matcher(version)
    return matcher.matches()
}

fun main() {
    val testVersions = arrayOf(
        "1.0.0",
        "2.1.3",
        "1.0.0-alpha",
        "1.0.0-alpha.1",
        "1.0.0-0.3.7",
        "1.0.0-x.7.z.92",
        "1.0.0+20130313144700",
        "1.0.0-beta+exp.sha.5114f85",
        "1.0.0-rc.1+build.1",
        "invalid-version",
        "1.0",
        "1.0.0-",
        "1.0.0+",

        "1.0.0",
        "2.1.3",
        "1.0.0-alpha",
        "1.0.0-alpha.1",
        "1.0.0-0.3.7",
        "1.0.0-x.7.z.92",
        "1.0.0+20130313144700",
        "1.0.0-beta+exp.sha.5114f85",
        "1.0.0-rc.1+build.1",
        "invalid-version",
        "1.0",
        "1.0.0-"
    )

    // Test ComparableVersion
    val startComparableVersion = System.nanoTime()
    repeat (1000) {
      for (version in testVersions) {
          isSemanticVersionUsingComparableVersion(version)
      }
    }
    val endComparableVersion = System.nanoTime()
    val comparableVersionTime = endComparableVersion - startComparableVersion

    // Test Regex
    val startRegex = System.nanoTime()
    repeat (2000) {
      for (version in testVersions) {
          isSemanticVersionUsingRegex(version)
      }
    }
    val endRegex = System.nanoTime()
    val regexTime = endRegex - startRegex

    // Test ComparableVersion again
    val startComparableVersion2 = System.nanoTime()
    repeat (2000) {
      for (version in testVersions) {
          isSemanticVersionUsingComparableVersion(version)
      }
    }
    val endComparableVersion2 = System.nanoTime()
    val comparableVersionTime2 = endComparableVersion2 - startComparableVersion2

    println("ComparableVersion time: $comparableVersionTime ns")
    println("Regex time: $regexTime ns")
    println("ComparableVersion 2nd time: $comparableVersionTime2 ns")
}
