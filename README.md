# field-agent

## Security Clearance

-- TODO

## Alias
### Domain Rules
- Name is required.
- Persona is not required unless a name is duplicated. The persona differentiates between duplicate names.

### Model
* `Alias`
    * private int aliasId;
    * private String name;
    * private String persona;
    * private int agentId;

### Fetch an individual agent with aliases attached.
* [ ] Add list of `Alias` to `Agent`
* [ ] Add `AliasMapper`
* [ ] Add method to aliases in `AgentJdbcTEmplateRepository.findById()`

### Add an alias
* [ ] Add `AliasRepository`
    * [ ] Add `add` method  `Alias add()` 
* [ ] Add `AliasService`
    * [ ] Add `add` method: `Result<Alias> add()`
    * [ ] Add validations
* [ ] Add `AliasController`
    * [ ] `ResponseEntity<Object> add()`
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
.. TODO