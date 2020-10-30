package thermos

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.*

class InstallBundle extends DefaultTask {
    @InputFile
    def File serverJar

    @InputFiles
    def ConfigurableFileCollection bootstrapClasspath

    @Input
    def String bootstrapMain

    InstallBundle() {
        bootstrapClasspath = project.files()
    }

    def bootstrapClasspath(Object... args) {
        bootstrapClasspath.from args
    }

    @OutputDirectory
    def File getInstallLocation() {
        new File(project.buildDir, 'bundle')
    }

    @TaskAction
    def install() {
        installLocation.deleteDir()
        installLocation.mkdirs()
        def cp = bootstrapClasspath
        for (int i = 0; i < 3; i++) {
            def result = project.javaexec { it ->
                workingDir installLocation
                classpath cp
                main bootstrapMain
                args '--serverDir', installLocation.canonicalPath,
                        '--installServer', serverJar.canonicalFile
            }
            if (result.exitValue == 0) return
        }
        throw new GradleException("Failed to install bundle into ${installLocation}")
    }

    private static final class NopOutputStream extends OutputStream {
        @Override
        void write(byte[] b, int off, int len) throws IOException {
        }

        @Override
        void write(byte[] b) throws IOException {
        }

        @Override
        void write(int b) throws IOException {
        }
    }
}
