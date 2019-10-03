## Savings - A Vert.x based example

An example of simple RESTful API implemented using Eclipse Vert.x framework, thus it's completely reactive. Yay! :-)

It showcases the usage of the following subset of Vert.x features:
* Web
* Config
* ServiceProxy
* WebClient
* JUnit5 integration

It also serve static files using `StaticHandler`, mounted for "/" requests and serving the files from `webroot` directory that exists in the root project structure. The alternative is to have this directory in `src/main/resources`, thus it gets bundled into the generated executable JAR, but during development any UI related change triggers an unnecessary redeploy of the verticles, so it not efficient this way.
<br/>

The business requirements (BRs) are as follows:
1. The account can only be opened between 8 AM and 5 PM.
2. The user can have only one savings account.


### Run

To run the application use either:
- `run.sh` to start it in the 'standard' mode (compile and run)
- `run-dev.sh` to start it in 'redeploy' mode


### Usage

There is a basic UI available at `http://localhost:8888`.

The following capabilities are exposed as API operations:

- Get a savings account by owner id
    - Example for owner id of 1 (that has a savings account):
      ```bash
      $ curl -v http://localhost:8888/savings/byowner/1
      {
        "id" : "5a32e3e7-3aab-4132-8a56-029a49fe759c",
        "name" : "My Account",
        "description" : "Holiday savings bucket",
        "ownerId" : "1"
      }
      $
      ```
    - If found, it returns the account data as JSON with HTTP RC 200.
    - Otherwise, it returns HTTP RC 404 with an empty body.
    
- Store a savings account by owner id
    - Example for owner with id 1:
      ```bash
      $ curl -H 'content-type: application/json' -d '{"name":"My Account","description":"Holiday savings bucket"}' http://localhost:8888/savings/byowner/1
      {
        "id" : "5a32e3e7-3aab-4132-8a56-029a49fe759c",
        "name" : "My Account",
        "description" : "Holiday savings bucket",
        "ownerId" : "1"
      }
      $
      ```

    - if BRs are satisfied, it returns the newly created account as Json and HTTP RC 201
    - otherwise, it returns HTTP RC 401 with a friendly error


### Unit Tests

To launch the tests use `./mvnw clean test`


### Build

To package the application use `./mvnw clean package`. It will run the tests again and generate `target/savings-vx-1.0.0-fat.jar` file.

Then you can simply run it in the same project root using `java -jar target/savings-vx-1.0.0-fat.jar`.

Note that:
- it is looking for `config/service-config.json` file which is a relative path
- it serves the static files from `webroot` directory

Therefore, if you deploy it somewhere, make sure you have the JAR plus `config` and `webroot` directories in the same target location.
