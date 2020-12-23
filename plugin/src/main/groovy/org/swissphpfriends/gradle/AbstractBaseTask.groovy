package org.swissphpfriends.gradle

import org.gradle.api.DefaultTask
import org.swissphpfriends.gradle.helper.SystemDependencies

abstract class AbstractBaseTask extends DefaultTask {

    Boolean verbose = false

    String executable = 'phpunit'

    String workingDirectory = './'

    protected SystemDependencies systemDependencies = new SystemDependencies();

    protected void checkPhpInstallation() {
        String exe = (System.getProperty("os.name").contains("Windows")) ? 'php.exe' : 'php';
        if (!this.systemDependencies.hasExecutableInPath(exe)) {
            throw new RuntimeException('PHP seems not installed or not available in PATH!')
        }
    }

    protected void printBuildMessage(String msg) {
        println("\n## " + msg + "\n----------------------------");
    }
}
