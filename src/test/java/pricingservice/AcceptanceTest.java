package pricingservice;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.StoryReporterBuilder;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;

public abstract class AcceptanceTest extends JUnitStory {

  public AcceptanceTest() {
    Embedder embedder = configuredEmbedder();
    embedder.useMetaFilters(getMetaFilters());
    embedder.embedderControls().doVerboseFailures(true)
            .useStoryTimeoutInSecs(60);
  }

  @Override
  public Configuration configuration() {
    return new MostUsefulConfiguration()
            .useStoryReporterBuilder(new StoryReporterBuilder()
                    .withDefaultFormats()
                    .withFormats(CONSOLE, HTML, TXT)
                    .withCodeLocation(
                            CodeLocations.codeLocationFromPath("build/jbehave")));
  }

  private List<String> getMetaFilters() {
    String metaFiltersProperty = System.getProperty("metaFilters", "");
    return Arrays.stream(metaFiltersProperty.split(","))
            .map(String::trim)
            .collect(toList());
  }
}
