package uk.gov.hmrc.jenkinsjobs.domain.builder

import uk.gov.hmrc.jenkinsjobbuilders.domain.builder.JobBuilder
import uk.gov.hmrc.jenkinsjobbuilders.domain.variable.EnvironmentVariable
import uk.gov.hmrc.jenkinsjobs.domain.variable.JavaHomeEnvironmentVariable
import uk.gov.hmrc.jenkinsjobs.domain.variable.JavaVersionEnvironmentVariable

import static uk.gov.hmrc.jenkinsjobbuilders.domain.publisher.ClaimBrokenBuildsPublisher.claimBrokenBuildsPublisher
import static uk.gov.hmrc.jenkinsjobbuilders.domain.publisher.CleanWorkspacePostBuildTaskPublisher.cleanWorkspacePostBuildTaskPublisher
import static uk.gov.hmrc.jenkinsjobbuilders.domain.trigger.PollTrigger.pollTrigger
import static uk.gov.hmrc.jenkinsjobs.domain.variable.ClasspathEnvironmentVariable.classpathEnvironmentVariable
import static uk.gov.hmrc.jenkinsjobs.domain.variable.TmpDirEnvironmentVariable.tmpDirEnvironmentVariable
import static uk.gov.hmrc.jenkinsjobs.domain.variable.PathEnvironmentVariable.pathEnvironmentVariable
import static uk.gov.hmrc.jenkinsjobbuilders.domain.wrapper.ColorizeOutputWrapper.colorizeOutputWrapper
import static uk.gov.hmrc.jenkinsjobbuilders.domain.wrapper.PreBuildCleanupWrapper.preBuildCleanUpWrapper
import static uk.gov.hmrc.jenkinsjobs.domain.scm.OrganisationGitHubComScm.hmrcGitHubComScm
import static uk.gov.hmrc.jenkinsjobs.domain.variable.JavaHomeEnvironmentVariable.javaHomeEnvironmentVariable
import static uk.gov.hmrc.jenkinsjobs.domain.variable.JavaVersionEnvironmentVariable.JDKLATEST

final class JobBuilders {

    private JobBuilders() {}

    static JobBuilder jobBuilder(String name, String repository) {
        jobBuilder(name, repository, 'master')
    }

    static JobBuilder jobBuilder(String name, String repository, String branch) {
        jobBuilder(name).
                   withScm(hmrcGitHubComScm(repository, branch)).
                   withTriggers(pollTrigger("H/5 * * * *")).
                   withLabel('single-executor').
                   withPublishers(cleanWorkspacePostBuildTaskPublisher())
    }

    static JobBuilder jobBuilder(String name) {
        new JobBuilder(name, "${name} auto-configured job").
                withLogRotator(7, 10).
                withEnvironmentVariablesFile('/var/lib/jenkins/internal-environment-variables.properties').
                withEnvironmentVariables(environmentVariables()).
                withWrappers(colorizeOutputWrapper(), preBuildCleanUpWrapper()).
                withPublishers(claimBrokenBuildsPublisher())
    }

    private static List<EnvironmentVariable> environmentVariables() {
        JavaVersionEnvironmentVariable javaVersionEnvironmentVariable = JDKLATEST
        JavaHomeEnvironmentVariable javaHomeEnvironmentVariable = javaHomeEnvironmentVariable(javaVersionEnvironmentVariable)
        [classpathEnvironmentVariable(), tmpDirEnvironmentVariable(), javaVersionEnvironmentVariable, javaHomeEnvironmentVariable, pathEnvironmentVariable(javaHomeEnvironmentVariable)]
    }
}
