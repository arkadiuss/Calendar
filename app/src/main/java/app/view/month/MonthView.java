package app.view.month;

import app.AppContext;
import app.di.DIProvider;
import com.google.common.collect.ImmutableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MonthView {

    private List<DayAnchorPane> monthDaysAnchors = new LinkedList<>();
    private VBox monthViewVBox;
    private YearMonth currentYearMonth;
    private AppContext appContext;

    public MonthView() {
        this.appContext = DIProvider.getAppContext();
        appContext.observeSelectedDate().subscribe((date) -> {
            currentYearMonth = YearMonth.of(date.getYear(), date.getMonth());
            setUpView();
        });
    }

    private void setUpView() {
        List<Label> dayLabels = ImmutableList.of("Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday", "Sunday")
                .stream().map(Label::new).collect(Collectors.toList());

        GridPane dayLabelsGrid = new GridPane();
        dayLabelsGrid.setStyle("-fx-background-color: #FFFFFF");
        int dayCount = 0;
        for (Label dayLabel : dayLabels) {
            dayLabel.setPrefSize(100, 30);
            dayLabel.setAlignment(Pos.BASELINE_CENTER);
            dayLabelsGrid.add(dayLabel, dayCount++, 0);
        }

        GridPane monthGrid = new GridPane();
        monthGrid.setStyle("-fx-background-color: #FFFFFF");
        monthGrid.setGridLinesVisible(true);

        long weeksCount = monthWeeksCount(currentYearMonth);

        for (int week_row = 0; week_row < weeksCount; week_row++) {
            for (int day_row = 0; day_row < 7; day_row++) {
                DayAnchorPane dayAnchorPane = new DayAnchorPane();
                dayAnchorPane.setPrefSize(100, 100);
                monthGrid.add(dayAnchorPane, day_row, week_row);
                monthDaysAnchors.add(dayAnchorPane);
            }
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
            dayAnchorPane.setDate(dateIterator);
            Label label = new Label(String.valueOf(dateIterator.getDayOfMonth()));
            AnchorPane.setLeftAnchor(label, 4.0);
            dayAnchorPane.getChildren().add(label);
            dayAnchorPane.setStyle("-fx-background-color: #FFFFFF;" +
                    " -fx-border-color: black; -fx-border-width: 1px 1px 1px 1px");


            dayAnchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
                    appContext.setSelectedDate(dayAnchorPane.getDate()));

            if (dateIterator.equals(LocalDate.now())) {
                dayAnchorPane.setStyle("-fx-background-color: #FFCFFF;" +
                        " -fx-border-color: black; -fx-border-width: 1px 1px 1px 1px");
            } else if (dateIterator.getMonth() == calendarStartDate.getMonth()) {
                dayAnchorPane.setStyle("-fx-background-color: #89CFFF;" +
                        " -fx-border-color: black; -fx-border-width: 1px 1px 1px 1px");
            }
            dateIterator = dateIterator.plusDays(1);
        }
    }

    private long monthWeeksCount(YearMonth yearMonth) {
        LocalDate startDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        return startDate
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .datesUntil(
                        endDate
                                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                                .plusWeeks(1),
                        Period.ofWeeks(1)
                )
                .map(localDate -> localDate.get(WeekFields.ISO.weekOfWeekBasedYear()))
                .count();
    }

    public VBox getMonthViewVBox() {
        return monthViewVBox;
    }
}