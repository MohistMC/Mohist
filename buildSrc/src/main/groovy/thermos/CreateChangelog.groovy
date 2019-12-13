package thermos

import org.gradle.api.GradleException
import org.gradle.api.GradleScriptException
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.copy.CopyAction
import org.gradle.api.internal.file.copy.CopyActionProcessingStream
import org.gradle.api.internal.tasks.SimpleWorkResult
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.WorkResult
import org.gradle.api.tasks.bundling.AbstractArchiveTask

public class CreateChangelog extends AbstractArchiveTask {
    @Input
    def String oldChangelogUrl
    @Input
    def String hash
    @Input
    def String message
    @Input
    def String version
    @Input
    def String format = "# <version>: <hash>\n<message>\n\n"

    @Override
    FileCollection getSource() {
        return project.files(project.buildDir)
    }

    @Override
    protected CopyAction createCopyAction() {
        return new CopyAction() {
            @Override
            WorkResult execute(CopyActionProcessingStream stream) {
                def oldChangelog;
                try {
                    oldChangelog = new URL(oldChangelogUrl).text.trim();
                } catch (Exception e) {
                    if (!project.hasProperty('ignoreOldChangelog'))
                        throw new GradleException('Error occurred during fetching latest log', e)
                    oldChangelog = ''
                    e.printStackTrace();
                }
                try {
                    def newMessage = '';
                    message.eachLine { newMessage += '   ' + it + '\n' }
                    def append = format.replace('<version>', version).replace('<hash>', hash).replace('<message>', message)
                    def changelog = append + oldChangelog;
                    archivePath.write(changelog.trim(), 'utf-8')
                    return new SimpleWorkResult(true);
                } catch(Exception e) {
                    e.printStackTrace();
                    return new SimpleWorkResult(false);
                }
            }
        }
    }
}
