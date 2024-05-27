#!/usr/bin/env groovy

def createJob(String instanceName, String repositoryName) {
  multibranchPipelineJob("incoming/${repositoryName}") {

    branchSources {
      branchSource {
        source {
          scmManager {
            id("ecosystem@scm-manager/${repositoryName}")
            credentialsId('scmCredentials')
            serverUrl("https://${instanceName}-public.cloudogu.net/scm")
            repository("${repositoryName}/git")
            traits {
              scmManagerBranchDiscoveryTrait()
//              pullRequestDiscoveryTrait {
//                excludeBranchesWithPRs(true)
//              }
            }
          }
        }

        strategy {
        }

      }
    }

    orphanedItemStrategy {
      discardOldItems {
        numToKeep(5)
      }
    }

    factory {
      pipelineBranchDefaultsProjectFactory {
        // The ID of the default Jenkinsfile to use from the global Config
        // File Management.
        scriptId('IncomingJenkinsfile')

        // If enabled, the configured default Jenkinsfile will be run within
        // a Groovy sandbox.
        useSandbox false
      }
    }
  }

  queue("${namespace}/${pluginName}")
}

def createFolders() {
  folder('incoming') {
    description('Incoming repositories from public instance')
  }
}

def createIncomingJenkinsfile() {
  configFiles {
    groovyScript {
      id('IncomingJenkinsfile')
      name('Jenkinsfile')
      comment('Jenkinsfile for incoming repositories')
      content('templates/Jenkinsfile.default')
    }
  }
}

//def createJobs() {
//  def namespace = premium ? "scm-manager-premium-plugins" : "scm-manager-plugins"
//  URL apiUrl = new URL("https://ecosystem.cloudogu.com/scm/api/v2/repositories/${namespace}?pageSize=1000")
//  def repositories = new groovy.json.JsonSlurper().parse(apiUrl)
//  repositories._embedded.repositories.each{ repo ->
//    createJob(repo.name, premium)
//  }
//}

def instanceName = jm.getParameters().instanceName
def repositoryName = jm.getParameters().repositoryName

if ("none".equals(repositoryName)) {
  createIncomingJenkinsfiles()
//} else if ("all".equals(pluginName)) {
//  createIncomingJenkinsfiles()
//  createFolders()
//  createJobs()
} else {
  createIncomingJenkinsfile()
  createFolders()
  createJob(instanceName, repositoryName)
}

