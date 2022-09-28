rootProject.name = "wy-space"

plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.0.20"
}

gitHooks {
    commitMsg { conventionalCommits() }
    preCommit {
        from {
            """
            ./gradlew check
            """
        }
    }
    createHooks()
}
