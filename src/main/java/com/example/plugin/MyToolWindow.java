package com.example.plugin;

import com.intellij.openapi.wm.ToolWindow;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyToolWindow {
    private static final String CALENDAR_ICON_PATH = "/toolWindow/Calendar-icon.png";
    private static final String TIME_ZONE_ICON_PATH = "/toolWindow/Time-zone-icon.png";
    private static final String TIME_ICON_PATH = "/toolWindow/Time-icon.png";

    FeatureContent featureContent;
    private final JPanel contentPanel = new JPanel();
    private final JFrame consoleFrame = new JFrame();

    private final JLabel content = new JLabel();
//    private final JLabel timeZone = new JLabel();
//    private final JLabel currentTime = new JLabel();

    public MyToolWindow(ToolWindow toolWindow) {
        consoleFrame.setPreferredSize(new Dimension(525, 600));
        consoleFrame.setFont(new Font("Lucida Console", Font.BOLD, 12));
        consoleFrame.setBackground(Color.BLACK);
        contentPanel.setLayout(new BorderLayout(0, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        contentPanel.add(createCalendarPanel(), BorderLayout.PAGE_START);
        contentPanel.add(createControlsPanel(toolWindow), BorderLayout.WEST);
        updateCurrentDateTime();
    }

    @NotNull
    private JPanel createCalendarPanel() {
        JPanel calendarPanel = new JPanel();
//        setIconLabel(currentDate, CALENDAR_ICON_PATH);
//        setIconLabel(timeZone, TIME_ZONE_ICON_PATH);
//        setIconLabel(currentTime, TIME_ICON_PATH);
        calendarPanel.add(content);
//        calendarPanel.add(timeZone);
//        calendarPanel.add(currentTime);
        return calendarPanel;
    }

    private void setIconLabel(JLabel label, String imagePath) {
        label.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))));
    }

    @NotNull
    private JPanel createControlsPanel(ToolWindow toolWindow) {
        JPanel controlsPanel = new JPanel();
        JButton refreshDateAndTimeButton = new JButton("Refresh");
        refreshDateAndTimeButton.addActionListener(e -> updateCurrentDateTime());
        controlsPanel.add(refreshDateAndTimeButton);
        JButton hideToolWindowButton = new JButton("Hide");
        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        controlsPanel.add(hideToolWindowButton);
        return controlsPanel;
    }

    private void updateCurrentDateTime() {
//        System.out.println(featureContent.getContent());
        Calendar calendar = Calendar.getInstance();
        content.setText("");
        Map<String, List<Object>> contentMap = FeatureContent.getContent();
        if(contentMap == null || contentMap.size() < 1) content.setText("No duplicate scenarios. All Good!!");
        else {
            for (var entry : contentMap.entrySet()) {
                StringBuffer sb = new StringBuffer();
                for (Object s : entry.getValue()) {
                    sb.append(s);
                    sb.append(" ");
                }
                String str = sb.toString();

                content.setText(content.getText() + "\n" + entry.getKey() + " Scenario is duplicate. Please check lines " + str);

            }
        }
//        timeZone.setText(getTimeZone(calendar));
//        currentTime.setText(getCurrentTime(calendar));
    }

    private String getCurrentDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "/"
                + (calendar.get(Calendar.MONTH) + 1) + "/"
                + calendar.get(Calendar.YEAR);
    }

    private String getTimeZone(Calendar calendar) {
        long gmtOffset = calendar.get(Calendar.ZONE_OFFSET); // offset from GMT in milliseconds
        String gmtOffsetString = String.valueOf(gmtOffset / 3600000);
        return (gmtOffset > 0) ? "GMT + " + gmtOffsetString : "GMT - " + gmtOffsetString;
    }

    private String getCurrentTime(Calendar calendar) {
        return getFormattedValue(calendar, Calendar.HOUR_OF_DAY) + ":" + getFormattedValue(calendar, Calendar.MINUTE);
    }

    private String getFormattedValue(Calendar calendar, int calendarField) {
        int value = calendar.get(calendarField);
        return StringUtils.leftPad(Integer.toString(value), 2, "0");
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

}
