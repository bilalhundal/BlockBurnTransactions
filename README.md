Project Overview
This project fetches the latest blocks and transactions data from the Hiro API, processes it, and stores the data in an AWS S3 bucket for further analysis. The project includes a Lambda function deployed using AWS SAM, which is scheduled to run at a periodic interval to ensure continuous data updates. The stored data in S3 can be connected to AWS QuickSight for insightful analysis of blockchain transactions.

Features
Fetches the latest blocks and their transactions from the Hiro API.
Avoids duplicate processing of blocks by maintaining a record of processed blocks in S3.
Periodically invokes the Lambda function to ensure updated data is stored in S3.
Supports analysis through AWS QuickSight by visualizing and interpreting the transaction data.
Prerequisites
Before setting up this project, ensure you have the following:

AWS CLI installed and configured with the required permissions.
AWS SAM CLI installed for deploying the project.
Java 17 and Maven installed.
Git installed for cloning the repository.
How to Clone and Run the Project
Step 1: Clone the Repository
bash
Copy code
git clone <repository-url>
cd <repository-folder>
Step 2: Build the Project
Ensure Maven is installed and build the project locally:

bash
Copy code
mvn clean package
Step 3: Deploy the Project using AWS SAM
Run the following commands to deploy the project to AWS:

Build the SAM application:

bash
Copy code
sam build
Deploy the SAM application:

bash
Copy code
sam deploy --guided
Follow the prompts to configure the deployment. Provide the following details:

Stack Name: A unique name for your SAM stack.
AWS Region: The region where you want to deploy the resources.
Bucket Name: The name of the S3 bucket to store transaction data.
Step 4: Verify Deployment
Go to the AWS Lambda Console to verify that the Lambda function is created.
Go to the EventBridge Console to ensure a rule is attached to the Lambda function for periodic invocation.
Check the S3 Console to confirm that the transaction data is being stored correctly.
Changing Lambda Function Execution Interval
The periodic execution interval is controlled by the ScheduleExpression in the template.yaml file.

Open the template.yaml file.

Locate the EventBridge rule:

yaml
Copy code
TransactionSchedulerRule:
  Type: AWS::Events::Rule
  Properties:
    ScheduleExpression: "rate(1 minute)"
Update the ScheduleExpression value to the desired interval, such as:

Every 2 minutes: "rate(2 minutes)"
Every hour: "rate(1 hour)"
Re-deploy the application to apply changes:

bash
Copy code
sam deploy
Using the Project for Block and Transaction Analysis
1. Data Storage
Blocks and Transactions: Data is fetched from the Hiro API and stored in JSON files in the S3 bucket.
Processed Blocks: A processed-blocks.json file in S3 keeps track of blocks that have already been processed to avoid duplication.
2. AWS QuickSight Integration
You can use AWS QuickSight to analyze the data stored in the S3 bucket:

Go to the AWS QuickSight Console.
Connect to S3:
Choose Manage Data > New Dataset > S3.
Select your bucket and provide the path to the JSON files.
Transform and Visualize:
Use QuickSight to transform JSON data into tabular format.
Create visualizations such as transaction counts, block trends, or asset-specific analysis.
Project Structure
bash
Copy code
/src/main/java/com/bilal/hundal1
├── handlers                # Lambda function handler classes
├── services                # Service classes for API calls and S3 interactions
├── configurations          # Configuration beans for RestTemplate and S3Client
├── BlockBurnTransactionsApplication.java # Main Spring Boot application class

template.yaml               # AWS SAM template for Lambda function, S3 bucket, and EventBridge rule
pom.xml                     # Maven dependencies and build configuration
README.md                   # Project documentation
Troubleshooting
Lambda Function Not Triggering:

Ensure the EventBridge rule is attached to the Lambda function.
Verify the interval in the ScheduleExpression matches your requirements.
Data Not Stored in S3:

Check the BUCKET_NAME environment variable in the Lambda function.
Confirm that the S3 bucket exists and has the correct permissions.
QuickSight Cannot Access Data:

Ensure the S3 bucket policy allows access from QuickSight.
Confirm the data format in JSON files is compatible with QuickSight transformations.
Customizations
API Endpoint: Update the API_BASE_URL environment variable in the template.yaml file for different Hiro API endpoints.
Analysis Enhancements: Extend the Lambda function to perform pre-analysis (e.g., aggregations) before storing data in S3.
