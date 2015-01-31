---
layout: post
title: Acceptance testing with JBehave and Gradle
author: grzegorz.witkowski
tags: [atdd, acceptance testing, jbehave, gradle, java]
---

### Very short introduction to ATDD

By each sprint applications we develop gain more and more features. Then it's easy to be unclear about certain
functionality developed some time ago. It's also hard to remember all the corner cases. So we write high level
acceptance tests that will describe expected behavior. After user story is implemented, the test joins a regression
suite that will protect application from bugs introduced in new stories.

One method of writing acceptance tests is ATDD. It's simply TDD where A stands for acceptance. In ATDD the first thing
we do when we implement new feature is to write acceptance tests. Of course initially they will fail, since feature is
missing. Then in next steps we develop the feature to satisfy acceptance tests. The great thing in this approach is that
we focus on implementing only features that we need, avoiding unnecessary code. Also overall architecture is driven by
user needs, reflected by tested API.

### Problems with specifying requirements in source code

We always hear that acceptance tests should be a living documentation for our projects. They should clearly describe
what are the outcomes for given inputs. But when we describe requirements in programming language then living
documentation concept often becomes just theory. Some languages and testing tools have nice syntactic sugar that
simplify reading, such as Groovy with its ability to use spaces to separate words in method names or Spock with
predefined given/when/then blocks, etc. But test specification written in programming language is always cluttered with
its keywords, import statements, indentation, setup blocks in different methods or even classes etc. We could also use
another
<a href="https://github.com/sf105/goos-code/blob/master/test/end-to-end/test/endtoend/auctionsniper/AuctionSniperEndToEndTest.java">approach</a>
presented by Steve Freeman and Nat Pryce in „Growing Object-Oriented Software Guided by Tests"
This tests hide complexities in wrapper classes that simulate invocations of application api and external conditions.
But it's still source code. If you need clean specification of application behavior, that people from business could easily
open and read, then you need something better than source code.

### Why JBehave?
There are many tools supporting ATDD, also for Java, such as <a href="http://jbehave.org">JBehave</a>,
<a href="https://github.com/cucumber/cucumber-jvm">Cucumber JVM</a>, <a href="http://concordion.org/">Concordion</a> and
others. You can search some articles comparing them. In this article we will use JBehave. Here are some of its pros:
- very popular
- well documented
- it's in Java/jUnit, you can run acceptance test from IDE like other unit tests
- specification in txt files, you only need a text editor to read them
- uses BDD approach with given/when/then blocks
- it's fast, just a thin wrapper around jUnit
- very configurable
- generates nice reports in HTML or TXT formats
- mature and feature rich

### Example project
As a reference for our JBehave tests we will use hypothetical Pricing Service, that will tell us how much we must pay
when we create offer in <a href="http://allegro.pl">allegro.pl</a>. Complete source code is available on github
<a href="#">???</a>. Price depends from chosen promo options and category where we put our offer. It's also different in
various categories like computers, fashion, sport, etc. In the background Pricing Service maintains several price lists.
One of them is default and contains price for all promo options. There could be also additional price lists for specific
categories that override price from default.

### First test
Here's the first test for our service. It consists of 3 simple steps labeled by keywords Given/When/Then. We will show 
later how JBehave knows which code should be invoked for these smart sentences.

`price_in_root_category_test.story`
```
Given price list for root category exists with:
| promoOption |   fee |
| BOLD        |  0.70 |
| HIGHLIGHT   |  1.99 |
| PHOTO       |  0.50 |
When creating offer in root category with promo options BOLD,PHOTO
Then price should equal 1.20
```

Now we need to configure JBehave. There are several ways to do this. We extend JUnitStory class and configure embedder
manually, as it gives control than using annotations. Other ways of configuration are listed in 
<a href="http://jbehave.org/reference/stable/configuration.html"documentation</a>.

`AcceptanceTest.java`
```java
public abstract class AcceptanceTest extends JUnitStory {

    public AcceptanceTest() {
        Embedder embedder = configuredEmbedder();
        embedder.embedderControls()
                .doVerboseFailures(true)
                .useStoryTimeoutInSecs(60);
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withFormats(CONSOLE, HTML, TXT)
                        .withCodeLocation(CodeLocations.codeLocationFromPath("build/jbehave")));
    }
}
```

JBehave will look for Java file corresponding to story file named PriceInRootCategoryTest.java. It must be located in
the same directory. So our directory structure looks like this:

```
└── src
    ├── main
    │   └── java
    │       └── pricingservice
   ...
    └── test
        └── java
            └── pricingservice
                ├── AcceptanceTest.java
                ├── PriceCalculationReference.java
                ├── steps
               ...
                └── stories
                    ├── PriceInRootCategoryTest.java
                    └── price_in_root_category_test.story
```

Test file PriceInRootCategoryTest.java extends our AcceptanceTest class and contains the rest of configuration, specific
for each test, eg. how to create classes with steps.

`PriceInRootCategoryTest.java`
```java
public class PriceInRootCategoryTest extends AcceptanceTest {

    @Override
    public InjectableStepsFactory stepsFactory() {
        PricingApi pricingApi = new PricingApi(new PriceListRepository());
        PriceCalculationReference priceCalculationReference = new PriceCalculationReference();
        return new InstanceStepsFactory(configuration(),
                new PriceInRootCategorySteps(pricingApi, priceCalculationReference),
                new PriceAssertionSteps(pricingApi, priceCalculationReference));
    }
}
```

Finally steps are simple POJOs that contain methods marked with @Given, @When and @Then annotations, that map to
sentences from story file.

`PriceInRootCategorySteps.java`
```java
public class PriceInRootCategorySteps {

    private static final int ROOT_CATEGORY = 0;

    private PricingApi pricingApi;
    private PriceCalculationReference priceCalculationReference;

    public PriceInRootCategorySteps(PricingApi pricingApi, PriceCalculationReference priceCalculationReference) {
        this.pricingApi = pricingApi;
        this.priceCalculationReference = priceCalculationReference;
    }

    @Given("price list for root category exists with: $fees")
    public void priceListForRootCategoryExistsWithFees(ExamplesTable fees) {
        PriceList priceList = toPriceList(fees);
        pricingApi.addPriceList(priceList, ROOT_CATEGORY);
    }

    @When("creating offer in root category with promo options $selectedPromoOptions")
    public void creatingOfferInRootCategoryWithPromoOptions(List<PromoOption> selectedPromoOptions) {
        PriceCalculation priceCalculation =
                pricingApi.calculatePrice(ImmutableSet.copyOf(selectedPromoOptions), ImmutableSet.of(ROOT_CATEGORY));
        this.priceCalculationReference.setCalculationId(priceCalculation.getCalculationId());
    }

    private PriceList toPriceList(ExamplesTable fees) {
        Map<PromoOption, BigDecimal> feesForPromoOptions = fees.getRows().stream()
                .collect(toMap(row -> PromoOption.valueOf(row.get("promoOption")), row -> new BigDecimal(row.get("fee"))));
        return new PriceList(feesForPromoOptions);
    }
}
```

Third step „Then" is common for many tests so it's located in different class.

```java
public class PriceAssertionSteps {

    private PricingApi pricingApi;
    private PriceCalculationReference priceCalculationReference;

    public PriceAssertionSteps(PricingApi pricingApi, PriceCalculationReference priceCalculationReference) {
        this.pricingApi = pricingApi;
        this.priceCalculationReference = priceCalculationReference;
    }

    @Then("price should equal $expPrice")
    public void priceShouldEqual(BigDecimal expPrice) {
        PriceCalculation priceCalculation = pricingApi.getPriceCalculation(priceCalculationReference.getCalculationId());
        assertThat(priceCalculation.getPrice()).isEqualTo(expPrice);
    }
}
```

It's worth to notice that in PriceInRootCategoryTest.java we control how steps classes are created and can pass to them
some dependencies. This is a big advantage of using ConfigurableEmbedder to configure JBehave, because when we use
provided junit runner `@RunWith(AnnotatedEmbedderRunner.class)` and declare steps in `@UsingSteps(instances =
{TraderSteps.class})` annotation, only default constructor can be called.

Now we can run PriceInRootCategoryTest.java in IDE as any other jUnit test and see how it passes. However we want to
configure our build tool to run acceptance tests and be able to run them on CI server. Inside Allegro we use
<a href="http://gradle.org">Gradle</a>. Unfortunately JBehave documentation doesn't say anything how to use it with
Gradle. There is also no plugin like jbehave-maven-plugin. But in the end it only requires small amount of magic.

`build.gradle`
```
apply plugin: 'java'

sourceCompatibility = 1.8
version = '1.0'

repositories {
    mavenCentral()
}

dependencies {
    compile 'com.google.guava:guava:18.0'
    testCompile 'junit:junit:4.11'
    testCompile 'org.assertj:assertj-core:1.7.0'
    testCompile 'org.jbehave:jbehave-core:3.9.5'
    testCompile 'org.jbehave.site:jbehave-site-resources:3.1.1:@zip'
}

sourceSets.test.resources.srcDir 'src/test/java'

test {
    doFirst {
        copy {
            from(zipTree(jarPath("jbehave-core"))) {
                include "style/*"
            }
            into("build/jbehave/view")

        }
        copy {
            from(zipTree(jarPath("jbehave-site-resources"))) {
                include "js/**/*"
                include "style/**/*"
                include "images/*"
            }
            into("build/jbehave/view")
        }
    }
}

def jarPath(String jarName) {
    configurations.testCompile.find({ it.name.startsWith(jarName) }).absolutePath
}

task wrapper(type: Wrapper) {
    gradleVersion = '2.2.1'
}

```

We need to add `src/test/java` with our story file to test resources, because gradle filters out non-java files. We also
must manually copy web resources into build dir. And thats all. Our acceptance tests are regular jUnit classes only
wrapped by JBehave extensions, so they are run always when we run `./gradlew test`

Now we can see pretty html output from the tests in `build/jbehave/view/reports.html`

<img src="jbehave-reports.png">

We can clik on the test name to ses its details

<img src="jbehave-root-category.png">

### Custom input parameters

The true strength of JBehave is its ability to provide input parameters in th form we like and understand. Sometimes one
image is worth thousand words. Of course JBehave couldn't parse images and magically extract input parameters, but we
can write it using ASCII characters in any way we want and then parse it. Let's say we want to test a feature that price
list in sub category will override values from default price list. We can write it in the sentences but let's see this:

`price_list_tree_test.story`
```
Narrative:
In order to increase number of offers
As a price list administrator
I want to lower price for some promo options in specific categories

Lifecycle:
Before:
Given no price lists are defined

Scenario: Price in category with redefined price list

Given price list configuration exists:

# 0: BOLD: 0.70, HIGHLIGHT: 1.00, PHOTO: 0.50
|
|----# 1: BOLD: 0.60, PHOTO: 0.40
|    |
|    |----# 2: BOLD: 0.40, PHOTO: 0.30
|
|----# 3: PHOTO: 0.40

When creating offer in category 0,1,2 with promo options BOLD,PHOTO,HIGHLIGHT
Then price should equal 1.70

When creating offer in category 0,3 with promo options BOLD,PHOTO
Then price should equal 1.10

Scenario: Price in category without explicitly defined price list

Given price list configuration exists:

# 0: BOLD: 0.70, HIGHLIGHT: 1.00, PHOTO: 0.50
|
|----# 1: BOLD: 0.60, PHOTO: 0.40
|    |
|    |---- 2

When creating offer in category 0,1,2 with promo options BOLD,PHOTO,HIGHLIGHT
Then price should equal 2.00
```

It's much better to visualize price list state in a form of a tree than explain it in words. The steps class looks like
this:

```java
public class PriceListTreeSteps {

    private PricingApi pricingApi;
    private PriceListRepository priceListRepository;
    private PriceCalculationReference priceCalculationReference;

    public PriceListTreeSteps(PricingApi pricingApi, PriceListRepository priceListRepository,
            PriceCalculationReference priceCalculationReference) {
        this.priceListRepository = priceListRepository;
        this.priceCalculationReference = priceCalculationReference;
        this.pricingApi = pricingApi;
    }

    @Given("no price lists are defined")
    public void noPriceListsAreDefined() {
        priceListRepository.clear();
    }

    @Given("price list configuration exists: $priceListTree")
    public void priceListConfigurationExists(String priceListTree) throws Exception {
        Map<Integer, PriceList> priceListsInCategories = new PriceListParser().parse(priceListTree);
        priceListsInCategories.entrySet().stream().forEach(entry -> {
            int category = entry.getKey();
            PriceList priceListForCategory = entry.getValue();
            pricingApi.addPriceList(priceListForCategory, category);
        });
    }

    @When("creating offer in category $categoryPath with promo options $promoOptions")
    public void creatingOfferInCategoryWithPromoOptions(List<Integer> categoryPath, List<PromoOption> promoOptions) {
        PriceCalculation priceCalculation = pricingApi.calculatePrice(ImmutableSet.copyOf(promoOptions),
                ImmutableSet.copyOf(categoryPath));
        this.priceCalculationReference.setCalculationId(priceCalculation.getCalculationId());
    }
}
```

Of course JBehave knows nothing about our domain and we need to provide a class to parse price list in the text form to
our data structure.

In the test above we also see something new. We could embed user story using Narrative keyword. There are also two
scenarios each with own description. Each scenario is preceded by steps from Before keyword, in this case removing price
lists added by previous test.

### Meta filtering
Another very useful feature is meta filtering. Using it we can separate stable regression tests that are run on CI
during each build from tests written for stories being currently implemented. Current stories are not finished, so its 
tests will fail. We want to keep CI green, so we can add tags <i>[regression]</i> and <i>[story 123]</i>, 
<i>[story 456]</i> and run only tests with tag regression.

`price_in_root_category_test.story`
```
Meta:
@regression

Given ...
```

`price_list_tree_test.story`
```
Meta:
@story 42

Narrative: ...
```

`AcceptanceTest.java`
```java
...
    public AcceptanceTest() {
        Embedder embedder = configuredEmbedder();
        embedder.useMetaFilters(getMetaFilters());
        embedder.embedderControls()
                .doVerboseFailures(true)
                .useStoryTimeoutInSecs(60);
    }
    
    private List<String> getMetaFilters() {
        return Arrays.stream(System.getProperty("metaFilters", "").split(","))
                .map(String::trim)
                .collect(toList());
    }
...
```

`build.gradle`
```
...
test {
    systemProperty "metaFilters", System.getProperty("metaFilters", "")
    ...
```

Now we can run only specified tests adding meta filter to Gradle command

`./gradlew test -DmetaFilters="-regression"`

### Conclusion
JBehave is great tool to implement acceptance tests. For sure it's easier to read specification in plain text than in 
source code. It's fast, and configurable. Of course real implementations will be more complex. Our example pricing 
serviceis is very simple. It runs only in memory and doestn't have any dependencies to data base or external services. 
In real project classes withs steps will be responsible to run local instance of service or connect to one deployed in 
test environment.
