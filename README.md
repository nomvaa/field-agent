# field-agent

## Security Clearance
### Delete Secuirty Clearance
* [ ] Add `boolean isReferenced` to `SecurityClearanceService` to check and see if security clearance Id is referenced anywhere. Only allow delete if a secuirty clearance key is NOT referenced in agency_agent table.


## Alias
### Domain Rules
* Name is required.
* Persona is not required unless a name is duplicated. The persona differentiates between duplicate names. 
<!-- unique id -->

### Examples
`name` = "Nutmeg", `persona` = null
`name` = "Nutmeg", `persona` = "Mysterious, like eggnog"
`name` = "Nutmeg", `persona` = "Mysterious, like eggnog" -- not allowed


### Model
* [x]`Alias`
    * private int aliasId;
    * private String name;
    * private String persona;
    * private int agentId;

### Fetch an individual agent with aliases attached.
* [x] Add list of `Alias` to `Agent`
* [x] Add `AliasMapper`
* [x] Add method to aliases in `AgentJdbcTEmplateRepository.findById()`

### Add an alias
* [x] Add `AliasRepository`
    * [x] Add `add` method  `Alias add()` 
* [x] Add `AliasService`
    * [x] Add `add` method: `Result<Alias> add()`
    * [x] Add validations
* [x] Add `AliasController`
    * [x] `ResponseEntity<Object> add()`
    * 404 is not found
    * 201 if success 

### Update an alias
* [ ] Respository `boolean update()`
* [ ] Service `Result<Alias> update()`
* [ ] Contoller `ResponseEntity<Object> update()`
    * 404 if not found
    * 400 if invalid
    * 204 if success

### Delete an alias. (No strategy required. An alias is never referenced elsewhere.)
* [ ] Service `boolean deleteById()`
* [ ] Service `boolean deleteById()`
* [ ] Controller `ResponseEntity<Object> deleteById()`
    * 404 if not found
    * 400 if invalid
    * 204 if success

## Global Exception Handling
### Use the @ControllerAdvice annotation
* register an exception handler for all controllers. Catch and handle exceptions at two levels.
* [ ] Determine the most precise exception for data integrity failures and handle it with a specific data integrity message.
* [ ] For all other exceptions, create a general "sorry, not sorry" response that doesn't share exception details.
