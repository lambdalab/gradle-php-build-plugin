package org.swissphpfriends.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ComposerInstallTask extends DefaultTask {

    @TaskAction
    def composerInstall() {
        if (!this.isAvailable()) {
            this.installPhar();

        } else {
            this.checkForSelfUpdate();
        }

        // install composer dependencies
        println "\n## installing dependencies using composer..."
        Process composerInstallProcess = new ProcessBuilder("php", "composer.phar", "install")
                .redirectErrorStream(true)
                .start()
        composerInstallProcess.inputStream.eachLine { println it }
        composerInstallProcess.waitFor()

        if (composerInstallProcess.exitValue()) {
            //throw new TaskExecutionException(this, new Throwable('command "php composer.phar install" failed!'))
            println "FAILED -.-"
        }
    }

    /**
     * @return boolean True if composer.phar is available and working
     */
    private boolean isAvailable() {
        def composerVersionProcess = new ProcessBuilder('php', 'composer.phar', '--version')
                .redirectErrorStream(true)
                .start()

        composerVersionProcess.waitFor();

        if (composerVersionProcess.exitValue()) {
            return false;
        }

        return true;
    }

    private void installPhar() {
        println "\n## composer.phar not presend in current directory. Installing..."

        // download installer
        def address = 'https://getcomposer.org/installer'
        def file = new FileOutputStream(address.tokenize("/")[-1])
        def out = new BufferedOutputStream(file)
        out << new URL(address).openStream()
        out.close()

        // install composer.phar
        def installComposerProcess = new ProcessBuilder('php', './installer')
                .redirectErrorStream(true)
                .start()

        installComposerProcess.inputStream.eachLine { println it }
        installComposerProcess.waitFor();

        if (installComposerProcess.exitValue()) {
            println "Composer installation failed!"
        }
    }

    private void checkForSelfUpdate() {
        println "\n## composer.phar already present. Checking for update..."

        Process composerSelfupdateProcess = new ProcessBuilder("php", "composer.phar", "selfupdate")
                .redirectErrorStream(true)
                .start()
        composerSelfupdateProcess.inputStream.eachLine { println it }
        composerSelfupdateProcess.waitFor()

        if (process.exitValue()) {
            println "Composer installation failed!"
        }
    }
}