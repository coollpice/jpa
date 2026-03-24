pipeline {
    agent any

    environment {
        // 프로젝트 명확화를 위해 변수 활용
        APP_NAME = "jpashop-ci-cd"
    }

    stages {
        stage('Checkout') {
            steps {
                // Git에서 소스 가져오기
                checkout scm
            }
        }

        stage('Gradle Build') {
            steps {
                sh 'chmod +x ./gradlew'
                // clean을 통해 이전 빌드 잔여물 제거
                sh './gradlew clean build -x test'
            }
        }

        stage('Copy to D Drive') {
            steps {
                script {
                    // 실행 가능한 jar 파일만 골라서 복사
                    // (보통 plain.jar는 용량이 매우 작으므로 구분 가능)
                    sh 'cp build/libs/*.jar /deploy/'

                    echo "🚀 빌드 완료! 호스트 D 드라이브 마운트 폴더를 확인하세요."
                }
            }
        }
    }

    post {
        success {
            echo "🎉 ${env.JOB_NAME} #${env.BUILD_NUMBER} 빌드 및 복사 성공!"
        }
        failure {
            echo '❌ 빌드 실패! 젠킨스 로그를 확인해 주세요.'
        }
    }
}