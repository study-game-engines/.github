@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package org.example

import com.google.gson.Gson
import java.io.File
import java.io.InputStream

data class Repository(val url: String, val description: String)

val Repository.name: String get() = url.removePrefix("https://github.com/study-game-engines/")
val exclude: List<String> = listOf(
    ".github",
    "mraid.js",
    "hazel",
    "hazel-dichotomy-demo",
    "hazel-forest-demo",
    "hazel-fragile-demo",
    "hazel-tools",
    "hazel.kt",
    "hazel.rs",
    "teachable-glsl-shaders-from-scratch",
)

fun main() {
    val file: InputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("repositories.json")
    val jsonText: String = file.reader().readText()
    val repositories: List<Repository> = Gson().fromJson(jsonText, Array<Repository>::class.java)
        .filter { !exclude.contains(it.name) }
        .sortedWith { repository1, repository2 -> repository1.name.compareTo(repository2.name) }
    File("REPOSITORIES.md").writer().use { writer ->
        writer.write("""
        # ${repositories.size} references to study game engines
        | Reference     | Description        |
        |:--------------|:-------------------|
        
        """.trimIndent())
        for (repository in repositories) {
            writer.write("| [${repository.name}](${repository.url}) | ${repository.description} |\n")
        }
        writer.flush()
    }
}