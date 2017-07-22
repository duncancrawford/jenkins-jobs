package uk.gov.hmrc.jenkinsjobs.domain.scm

import uk.gov.hmrc.jenkinsjobbuilders.domain.scm.Scm

import static uk.gov.hmrc.jenkinsjobbuilders.domain.scm.GitHubComScm.gitHubComScm


class OrganisationGitHubComScm implements Scm {
    private static final String JENKINS_UUID = 'ce814d36-5570-4f1f-ad70-0a8333122be6'
    private final Scm scm

    private final gitOrganisation = System.getProperty("git.organisation", "hmrc")

    private OrganisationGitHubComScm(String name, String branch) {
        scm = gitHubComScm("$gitOrganisation/$name", JENKINS_UUID, branch)
    }

    static Scm hmrcGitHubComScm(String name, String branch = 'master') {
        new OrganisationGitHubComScm(name, branch)
    }

    @Override
    Closure toDsl() {
        scm.toDsl()
    }
}
