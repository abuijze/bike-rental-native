# Bike Rental demo - Native edition

This is the Spring Native version of the Bike Rental Demo application. Code-wise, it is not
significantly different than the regular version. For simplicity, we have only removed the 
`@Profile` annotations from the different components, to run the entire application by default.

The project depends on the experimental Spring Native library, as well as the experimental 
Axon Spring Native support. The latter is currently not yet available in a public repository.

This code is experimental and very likely to change. It may work, and it may not :)

## Building the demo

As the axon-spring-native module isn't publicly available yet, you need to build that youself:

1. Clone the Axon Framework repository: `https://github.com/AxonFramework/AxonFramework.git`
2. Check out the `native-support` branch: `git checkout native-support`
3. Install it in your maven repo: `mvn install -DskipTests=true`

Now you're ready to build the project:

A "regular" build:
1. Make sure GraalVM is installed
2. Compile to native image using: `mvn clean package -DskipTests=true -Pnative` (skipping tests is not strictly necessary, but speeds up the build. It's just a demo, after all)

Build a Docker image using Build Packs:
Just run: `mvn spring-boot:build-image`

## Running the demo

By default, the demo will attempt to connect to a local Axon Server installation.
If you don't have one running, you can easily start one in Docker:

```bash
docker run --name axonserver -p 8024:8024 -p 8124:8124 -d axoniq/axonserver:latest
```

Then, start the application. Check the `target` folder for the executable. It should be called `bikerental`.

You can also connect the Bike Rental App to AxonIQ Cloud. Simply create your context and binding there. Create an `application.properties` file to place the configuration elements for your binding. It should look like:
```properties
axon.axonserver.servers=axonserver.cloud.axoniq.io:443
axon.axonserver.ssl-enabled=true
axon.axonserver.context=bike-rental@my-demo-space
axon.axonserver.token=##some secret token here###
axon.axonserver.keep-alive-time=0
```