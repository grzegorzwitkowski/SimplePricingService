# Simple Pricing Service
Simple implementation of pricing service, which calculates prices for creating offers. See article on 
[allegrotech.io](http://allegrotech.io/acceptance-testing-with-jbehave-and-gradle.html) blog.

### Building project

```
./gradlew build
```

### Running tests

```
./gradlew test
```

### Reports

Open reports located in build directory in web browser.

```
build/jbehave/view/reports.html
```

### Run only tests marked with a tag

Use `+` for inclusion and `-` for exclusion.

```
./gradlew test -DmetaFilters="+regression, -story *"
```
