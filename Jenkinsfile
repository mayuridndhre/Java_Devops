pipeline {
  agent any

  options {
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '15'))
  }

  parameters {
    choice(name: 'ENV', choices: ['dev', 'qa', 'prod'], description: 'Choose target environment')
  }

  triggers {
    // Optional: webhook असेल तरी ठेवू शकतो, नसल्यास हे Polling काम करेल
    pollSCM('H/2 * * * *') // ~प्रत्येक 2 मिनिटांनी बदल पाहतो
  }

  stages {
    stage('Checkout') {
      steps {
        // PUBLIC repo:
        git url: 'https://github.com/mayuridndhre/Java_Devops', branch: 'main'
      }
    }

    stage('Build') {
      steps {
        bat '''
        echo === BUILD START ===
        echo Date: %date% %time%
        if not exist build mkdir build
        echo Sample artifact created by Jenkins on %date% %time% > build\\artifact.txt
        echo === BUILD END ===
        '''
      }
    }

    stage('Archive Artifact') {
      steps {
        archiveArtifacts artifacts: 'build\\artifact.txt', onlyIfSuccessful: true
      }
    }

   stage('Deploy') {
  when { expression { params.ENV in ['dev','qa','prod'] } }
  steps {
    bat """
    set TARGET_DIR=C:\\deploy\\%ENV%
    if not exist %TARGET_DIR% mkdir %TARGET_DIR%

    if not exist C:\\wrongpath\\ (
        echo ❌ ERROR: Target directory does not exist!
        exit /b 1
    )

    copy /Y build\\*.txt C:\\wrongpath\\
    echo Deployed artifact to %TARGET_DIR%
    """
  }

    }
  }

  post {
    success {
      echo "✅ ${env.JOB_NAME} #${env.BUILD_NUMBER} finished OK (ENV=${params.ENV})"
    }
    failure {
      // Requires 'Email Extension' plugin + SMTP configured
      // to: मध्ये तुझा ईमेल टाक
      emailext(
        subject: "❌ Jenkins FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
        to: 'mayuridandhare62@gmail.com',
        body: """Build failed.

Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
ENV: ${params.ENV}
Console: ${env.BUILD_URL}console
"""
      )
    }
  }
}
