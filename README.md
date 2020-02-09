# Search Demo

The search demo service is a Gradle/Kotlin/Spring Web MVC/Junit 5/H2 web application.  It demonstrates
rudimentary searching capabilities on a relational database

## Prerequisites

The application requires Java 11 to run.  Java 11 was chosen since it is the most recent LTS version
of java.

The application was tested on and built Java 13.

## Running tests

```bash
./gradlew clean test
```

## Running the Application

The application runs on port 8080

```bash
./gradlew bootRun
```

## Accessing the API

API Documentation is available at `http://localhost:8080/swagger-ui.html`

I chose three separate endpoints since there were three separate data types.

These are the three root APIs:

```bash
http://localhost:8080/organizations
http://localhost:8080/users
http://localhost:8080/tickets
```

I added a `getAll` and `findById` method in these APIs mostly for my own testing purposes.  Since they 
fit the normal rest format, I left them.

## Search Format

I picked a very simple query format for this project: comma separated `key=value` pairs.  All `key=value` pairs
will be `ANDed` together due to time constraints.  If this was a fully fleshed out app, I could
convert the comma separator into `&` or `|` and build the criteria with `AND` or `OR` instead of 
just defaulting to `AND`.

Search currently only happens on the root object since I assumed this API would be consumed by some
sort of front end.  If search is supposed to happen on embedded objects, the query builder would just
need to be expanded to include a join and add the corresponding `Specification` to the query.

Partial matches are supported for any `String` type queries.

Invalid `value` queries are returned as a `400 BAD_REQUEST` with information about what field is invalid and 
what type is expected.

Invalid `key` queries are ignored.

```bash
curl -X GET \
  'http://localhost:8080/organizations/search?query=id=102,name=Nutralab'
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0/)



