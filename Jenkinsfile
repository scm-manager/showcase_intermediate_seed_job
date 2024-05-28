node {

  properties([
    parameters([
      string(name: 'instanceName', trim: true, description: "The URL of the public instance", defaultValue: 'none'),
      string(name: 'repositoryName', trim: true, description: "The name of the repository", defaultValue: 'none'),
    ])
  ])

  stage('Checkout') {
    checkout scm
  }

  stage('Create Job') {
    if (env.BRANCH_NAME == 'main') {
      currentBuild.description = params.pluginName
      jobDsl targets: 'plugin.groovy'
    } else {
      echo 'skip, executing job dsl in order to avoid bringing an unfinished feature live.'
    }
  }
}

def pluginNameDescription() {
  "Name of the repository that shall be checked."
}
