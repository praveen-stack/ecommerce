# Serverless

This project contains the serverless code for the ecommerce project


# Setup

- Install node js and npm
- Install serverless framework using npm

```npm i serverless -g```


# Deploying the apis and lambda functions

1. Source the environment vars. Sample file in bin/sample.sh
```
source ./bin/dev.sh
```

2. Build the source to generate the jar

```
maven package
```

3. To deploy to a specific stage 'dev' use command below. Change stage as per need.

```
sls deploy --stage dev
```


