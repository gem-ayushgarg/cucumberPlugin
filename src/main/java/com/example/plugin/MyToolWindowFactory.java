package com.example.plugin;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.wm.ToolWindowFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Objects;

public class MyToolWindowFactory implements ToolWindowFactory, DumbAware {

//    static FeatureContent featureContent = new FeatureContent();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
//        VirtualFile requiredFile = project.();
//        System.out.println(requiredFile);
//        InputStream stream = null;
//        String featureFileContent = null;
//        try {
//            stream = requiredFile.getInputStream();
//            featureFileContent = IOUtils.toString(stream, StandardCharsets.UTF_8);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//        featureContent.setContent(featureFileContent);
        MyToolWindow toolWindowContent = new MyToolWindow(toolWindow);
        Content content = ContentFactory.SERVICE.getInstance().createContent(toolWindowContent.getContentPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
