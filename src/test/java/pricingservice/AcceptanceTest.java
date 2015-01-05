package pricingservice;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.StoryReporterBuilder;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;

public abstract class AcceptanceTest extends JUnitStory {

    public AcceptanceTest() {
        configuredEmbedder().embedderControls()
                .doVerboseFailures(true)
                .useStoryTimeoutInSecs(60);
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withFormats(CONSOLE, HTML)
                        .withCodeLocation(CodeLocations.codeLocationFromPath("build/jbehave")));
    }
}
