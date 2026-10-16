## AWS EC2 Deployment — Java Interview Revision (Under 2 Minutes)

Interview question: How do you deploy a Spring Boot application on AWS EC2?

Interview-ready answer:

“I would first build my Spring Boot application using Maven to generate an executable JAR file. Then, I would launch an EC2 instance using an Amazon Linux AMI, configure the security group, and connect to the instance using SSH and a PEM key pair.

Next, I would install the compatible Java runtime, transfer the JAR using SCP, and execute it using `java -jar app.jar`. Spring Boot starts its embedded Tomcat server, typically on port 8080.

To make the application accessible externally, I would configure the security group’s inbound rules to allow the required traffic. For production, I would avoid exposing port 8080 publicly and instead use an Application Load Balancer with HTTPS.

I would also configure background execution, centralized logging, monitoring, and automated deployment through a CI/CD pipeline.”

## Important commands to remember

```
# 1. Build executable JAR
./mvnw clean package

# 2. Connect to EC2
ssh -i app-key.pem ec2-user@<EC2-PUBLIC-IP>

# 3. Copy JAR to EC2 (run locally)
scp -i app-key.pem target/app.jar \
ec2-user@<EC2-PUBLIC-IP>:/home/ec2-user/

# 4. Verify Java
java -version

# 5. Run application
java -jar app.jar

# 6. Run in background and capture logs
nohup java -jar app.jar > output.log 2>&1 &

# 7. Check application logs
tail -f output.log
```

## Five AWS concepts interviewers may ask

| Concept        | Interview answer                                                                                         |
| -------------- | -------------------------------------------------------------------------------------------------------- |
| EC2            | Virtual server in the AWS cloud.                                                                         |
| Security Group | Stateful virtual firewall controlling inbound and outbound traffic.                                      |
| SSH / SCP      | SSH provides remote access; SCP securely copies files.                                                   |
| IAM Role       | Grants AWS permissions to an application or EC2 instance without hardcoding credentials.                 |
| CI/CD          | Automates building, testing, and deploying applications using tools such as Jenkins or AWS CodePipeline. |

Important production-level improvements:

- High availability: Deploy across multiple Availability Zones behind an Application Load Balancer.
- Secrets: Use AWS Secrets Manager or Systems Manager Parameter Store.
- Monitoring: Use Amazon CloudWatch for metrics, logs, and alarms.
- Deployment: Automate releases rather than manually copying JAR files.
- Networking: Keep EC2 instances private where possible; expose HTTPS through the load balancer.

One follow-up question to prepare: Why would you choose EC2 instead of AWS Elastic Beanstalk, ECS, or Lambda?

For a Java developer with 3+ years of experience, focus on understanding the deployment flow, security groups, IAM, CloudWatch, load balancing, and CI/CD—not just memorizing terminal commands.