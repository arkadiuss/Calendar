package app.view.month;

import com.google.common.collect.ImmutableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MonthView {
    private List<DayAnchorPane> monthDaysAnchors = new LinkedList<>();
    private VBox monthViewVBox;
    private YearMonth currentYearMonth;


    public MonthView(YearMonth yearMonth) {
        currentYearMonth = yearMonth;

        GridPane monthGrid = new GridPane();
        monthGrid.setGridLinesVisible(true);
        for (int week_row = 0; week_row < 5; week_row++) {
            for (int day_row = 0; day_row < 7; day_row++) {
                DayAnchorPane dayAnchorPane = new DayAnchorPane();
                dayAnchorPane.setPrefSize(100, 100);
                monthGrid.add(dayAnchorPane, day_row, week_row);
                monthDaysAnchors.add(dayAnchorPane);
            }
        }

        List<Label> dayLabels = ImmutableList.of("Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday", "Sunday")
                .stream().map(Label::new).collect(Collectors.toList());

        GridPane dayLabelsGrid = new GridPane();
        int dayCount = 0;
        for (Label dayLabel : dayLabels) {
            dayLabel.setPrefSize(100, 30);
            dayLabel.setAlignment(Pos.BASELINE_CENTER);
            dayLabelsGrid.add(dayLabel, dayCount++, 0);
        }

        fillDayNumbers();
        monthViewVBox = new VBox(dayLabelsGrid, monthGrid);

    }


    public void fillDayNumbers() {
        LocalDate calendarStartDate = LocalDate.of(currentYearMonth.getYear(),
                currentYearMonth.getMonthValue(), 1);
        LocalDate dateIterator = calendarStartDate;

        while (!dateIterator.getDayOfWeek().toString().equals("MONDAY")) {
            dateIterator = dateIterator.minusDays(1);
        }

        for (DayAnchorPane dayAnchorPane : monthDaysAnchors) {
            Label label = new Label(String.valueOf(dateIterator.getDayOfMonth()));
            dayAnchorPane.setLeftAnchor(label, 4.0);
            dayAnchorPane.getChildren().add(label);

            if(dateIterator.equals(LocalDate.now())){
                dayAnchorPane.setStyle("-fx-background-color: #FFCFFF;" +
                        " -fx-border-color: black; -fx-border-width: 1px 1px 1px 1px");
            }else if (dateIterator.getMonth() == calendarStartDate.getMonth()) {
                dayAnchorPane.setStyle("-fx-background-color: #89CFFF;" +
                        " -fx-border-color: black; -fx-border-width: 1px 1px 1px 1px");
            }
            dateIterator = dateIterator.plusDays(1);
        }
    }


    public VBox getMonthViewVBox() {
        return monthViewVBox;
    }
}