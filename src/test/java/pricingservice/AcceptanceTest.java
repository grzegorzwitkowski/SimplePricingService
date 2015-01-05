package pricingservice;

import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.List;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(storyReporterBuilder = AcceptanceTest.MyReportBuilder.class)
@UsingEmbedder(embedder = Embedder.class, verboseFailures = true, storyTimeoutInSecs = 60)
@UsingSteps(instances = {PriceListTreeSteps.class, PriceInRootCategorySteps.class})
public class AcceptanceTest extends InjectableEmbedder {

    public static class MyReportBuilder extends StoryReporterBuilder {
        public MyReportBuilder() {
            URL customCodeLocation = CodeLocations.codeLocationFromPath("build/jbehave");
            withDefaultFormats().withFormats(CONSOLE, HTML).withCodeLocation(customCodeLocation);
        }
    }

    @Test
    public void run() {
        List<String> storyPaths = findStoryPathForClass();

        injectedEmbedder().runStoriesAsPaths(storyPaths);
    }

    private List<String> findStoryPathForClass() {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromPath("src/test/resources/"), "**/*.story", "");
    }
}
