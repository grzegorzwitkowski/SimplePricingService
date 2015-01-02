package pricingservice;

import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(storyReporterBuilder = AcceptanceTest.MyReportBuilder.class)
@UsingEmbedder(embedder = Embedder.class, verboseFailures = true, storyTimeoutInSecs = 60)
public class AcceptanceTest extends InjectableEmbedder {

    public static class MyReportBuilder extends StoryReporterBuilder {
        public MyReportBuilder() {
            URL customCodeLocation = CodeLocations.codeLocationFromPath("build/jbehave");
            withDefaultFormats().withFormats(CONSOLE, HTML).withCodeLocation(customCodeLocation);
        }
    }

    @Test
    public void run() {
        List<String> storyPaths = findStoryPathForClass(getClass());

        injectedEmbedder().runStoriesAsPaths(storyPaths);
    }

    private List<String> findStoryPathForClass(Class<? extends AcceptanceTest> acceptanceTestClass) {
        String acceptanceTestClassName = acceptanceTestClass.getSimpleName();
        Pattern pattern = Pattern.compile("([A-Z].*?)([A-Z]|\\z)");
        Matcher matcher = pattern.matcher(acceptanceTestClassName);
        int startAt = 0;
        StringBuilder builder = new StringBuilder();
        while (matcher.find(startAt)) {
            builder.append(matcher.group(1).toLowerCase());
            builder.append("_");
            startAt = matcher.start(2);
        }
        String storyNameString = builder.substring(0, builder.length() - 1) + ".story";

        return new StoryFinder().findPaths(CodeLocations.codeLocationFromPath("src/test/resources/"), "**/*.story", "")
                .stream().filter(path -> path.endsWith(storyNameString)).collect(Collectors.toList());
    }
}
