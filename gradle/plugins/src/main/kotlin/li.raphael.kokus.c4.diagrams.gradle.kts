@CacheableTask
abstract class MergeModuleGraphTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val slices: ConfigurableFileCollection

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun merge() {
        val parts = slices.files.sortedBy { it.absolutePath }.map { it.readText() }
        val json =
            buildString {
                append("{\"modules\":[")
                append(parts.joinToString(","))
                append("]}")
            }
        outputFile.get().asFile.also { out ->
            out.parentFile.mkdirs()
            out.writeText(json)
        }
    }
}

val merge =
    tasks.register("writeModuleGraph", MergeModuleGraphTask::class.java) {
        // fileTree avoids holding other Project instances; it just scans the FS at execution
        slices.from(
            fileTree(rootDir) {
                include("**/build/reports/module-deps.json")
            },
        )
        outputFile.set(layout.buildDirectory.file("reports/module-graph.json"))
    }

gradle.allprojects {
    val subProject = this
    if (subProject == rootProject) return@allprojects
    merge.configure {
        if (subProject.buildFile.exists()) {
            dependsOn("${subProject.path}:writeModuleDeps")
        }
    }
}
