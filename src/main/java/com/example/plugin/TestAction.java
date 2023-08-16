package com.example.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.qmetry.qaf.automation.step.client.Scenario;
import com.qmetry.qaf.automation.step.client.gherkin.GherkinFileParser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile requiredFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        String extension = requiredFile.getExtension();
        InputStream stream = null;
        String content = null;
        Map<String, List<Object>> finalMap;
        try {
            stream = requiredFile.getInputStream();
            content = IOUtils.toString(stream, StandardCharsets.US_ASCII);
            GherkinFileParser parser = new GherkinFileParser();
            List<Scenario> scenarios = new ArrayList<Scenario>();
            Map<Object, String> scenarioMap = new HashMap<>();
            List<String> duplicateScenarios = new ArrayList<>();
            finalMap = new HashMap<>();

            parser.parse(requiredFile.getPath(), scenarios);
            for (Scenario scenario : scenarios
            ) {
                scenarioMap.put(scenario.getMetadata().get("lineNo"), scenario.getTestName());
            }
            Map<String, Long> scenarioMapCount = scenarioMap
                    .values()
                    .stream()
                    .collect(
                            Collectors.groupingBy(Function.identity(),
                                    HashMap::new,
                                    Collectors.counting())
                    );
            scenarioMapCount
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > 1)
                    .forEach(en -> duplicateScenarios.add(String.valueOf(en).substring(0, en.toString().length() - 2)));

            for (String duplicateScenario : duplicateScenarios
            ) {
                ArrayList<Object> lineNumbers = new ArrayList<>();
                for (var entry : scenarioMap.entrySet()) {
                    if (entry.getValue().equals(duplicateScenario)) {
                        lineNumbers.add(entry.getKey());
                        finalMap.put(duplicateScenario, lineNumbers);
                    }
                }
            }

            System.out.println(finalMap);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        FeatureContent.setContent(finalMap);
//        String gitHubQuery = "https://github.com/search?q=%s&type=code";
//        String searchBy = String.format(gitHubQuery, URLUtil.encodeURIComponent(content));
//        BrowserUtil.browse(searchBy);
    }

    @Override
    public void update(@NotNull AnActionEvent e)
    {
        VirtualFile requiredFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        String extension = requiredFile.getExtension();

        if (!StringUtils.equalsIgnoreCase("feature", extension))
        {
            e.getPresentation().setVisible(false);
        }
    }
}

