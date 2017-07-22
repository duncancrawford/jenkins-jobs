# jenkins-jobs

Automated Jenkins jobs and views, based upon [jenkins-job-builders](https://github.com/hmrc/jenkins-job-builders)

## Building

Run `./gradlew clean test` locally to test your changes. The test suite will ensure the job definitions are capable of producing the expected config XML for Jenkins.

## System Variables

 Variable | Default 
 -------- | ------- 
 git.organisation | hmrc      
 java.version | 1.8.0_131      

 

## License

This code is open source software licensed under the Apache 2.0 License.
