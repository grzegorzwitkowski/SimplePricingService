package pricingservice;

import com.google.common.collect.ImmutableList;
import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryPathResolver;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(storyReporterBuilder = AcceptanceTest.MyReportBuilder.class)
@UsingEmbedder(embedder = Embedder.class, verboseFailures = true, storyTimeoutInSecs = 60)
public abstract class AcceptanceTest extends InjectableEmbedder {

    public static class MyReportBuilder extends StoryReporterBuilder {
        public MyReportBuilder() {
            URL customCodeLocation = CodeLocations.codeLocationFromPath("build/jbehave");
            withDefaultFormats().withFormats(CONSOLE, HTML).withCodeLocation(customCodeLocation);
        }
    }

    @Test
    public void run() {
        StoryPathResolver storyPathResolver = injectedEmbedder().configuration().storyPathResolver();
        String storyPath = storyPathResolver.resolve(this.getClass());
        injectedEmbedder().runStoriesAsPaths(ImmutableList.of(storyPath));
    }
}
