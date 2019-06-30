## Savings - A Vert.x based example



### Run

To run the application use either:
- `run.sh` to start it in the standard way (compile and run)
- `run-dev.sh` to start it in 'redeploy' mode


### Usage

The following features exposed as API operations can be used:

- get a savings account by owner id
    - example for owner id of 1 (that has a savings account):
      ```bash
      $ curl -v http://localhost:8888/savings/byowner/1
      {
        "id" : "5a32e3e7-3aab-4132-8a56-029a49fe759c",
        "name" : "My Account",
        "description" : "Holiday savings bucket",
        "ownerId" : "1"
      }
      ```
    - if found, it returns the account as Json and HTTP RC 200
    - otherwise, it returns HTTP RC 404 with an empty body
    
- store a savings account by owner id
    - example for owner id of 1:
      ```bash
      $ curl -H 'content-type: application/json' -d '{"name":"My Account","description":"Holiday savings bucket"}' http://localhost:8888/savings/byowner/1
      {
        "id" : "5a32e3e7-3aab-4132-8a56-029a49fe759c",
        "name" : "My Account",
        "description" : "Holiday savings bucket",
        "ownerId" : "1"
      }
      ```

    - if BRs are satisfied, it returns the newly created account as Json and HTTP RC 201
    - otherwise, it returns HTTP RC 401 with a friendly error

The BRs are as follows:
1. The account can only be opened between 8 AM and 5 PM.
2. The user can have only one savings account.

_________________________


To launch the tests use `./mvnw clean test`
To package the application use `./mvnw clean package`

