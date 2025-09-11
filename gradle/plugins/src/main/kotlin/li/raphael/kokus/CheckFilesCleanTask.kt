package li.raphael.kokus

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import org.gradle.work.DisableCachingByDefault
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

@DisableCachingByDefault(because = "Validation task - checks VCS state, not cacheable")
abstract class CheckFilesCleanTask
    @Inject
    constructor() : DefaultTask() {
        @get:InputFiles
        @get:PathSensitive(PathSensitivity.RELATIVE)
        abstract val targetFiles: ConfigurableFileCollection

        @get:InputDirectory
        @get:Optional
        abstract val gitWorkDir: DirectoryProperty

        @get:Inject
        abstract val execOps: ExecOperations

        init {
            group = "verification"
            description = "Fails if any target file has uncommitted/untracked changes"
            gitWorkDir
        }

        @TaskAction
        fun run() {
            val workDir = gitWorkDir.asFile.getOrElse(File(""))
            val git = Git(execOps, workDir)

            val files = targetFiles.files
            if (files.isEmpty()) {
                throw GradleException("No files provided for check")
            }

            val problems =
                files.mapNotNull { file ->
                    val displayPath = file.relativeToOrSelf(workDir).path
                    when {
                        git.isUntracked(file) -> "File '$displayPath' is untracked"
                        git.isDirty(file) -> "File '$displayPath' has uncommitted changes"
                        else -> null
                    }
                }

            if (problems.isNotEmpty()) {
                throw GradleException(
                    buildString {
                        appendLine("Detected uncommitted or untracked files:")
                        problems.forEach { appendLine("- $it") }
                    },
                )
            }
        }
    }

private class Git(
    private val execOps: ExecOperations,
    private val workDir: File,
) {
    init {
        if (!isInsideWorkTree()) {
            throw GradleException("Not inside a git repository (workDir: ${workDir.absolutePath})")
        }
    }

    fun isDirty(file: File): Boolean = !execOk("git", "diff", "--quiet", "HEAD", "--", file.absolutePath)

    fun isUntracked(file: File): Boolean =
        execOutput(
            "git",
            "ls-files",
            "--others",
            "--exclude-standard",
            "--",
            file.absolutePath,
        ).isNotBlank()

    private fun isInsideWorkTree(): Boolean = execOk("git", "rev-parse", "--is-inside-work-tree")

    private fun execOk(vararg cmd: String): Boolean {
        val result =
            execOps.exec {
                workingDir = workDir
                commandLine = cmd.toList()
                isIgnoreExitValue = true
                standardOutput = ByteArrayOutputStream()
            }
        return result.exitValue == 0
    }

    private fun execOutput(vararg cmd: String): String {
        val stdOut = ByteArrayOutputStream()
        execOps.exec {
            workingDir = workDir
            commandLine = cmd.toList()
            isIgnoreExitValue = false
            standardOutput = stdOut
        }
        return stdOut.toString(Charsets.UTF_8)
    }
}
