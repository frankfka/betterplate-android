package app.betterplate.betterplate.service;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ViewHelperService {

    /**
     * Style to standard chart implementation
     */
    public static void setUpNutritionPieChart(PieChart chart,
                                              float percentageCarbs,
                                              float percentageFat,
                                              float percentageProtein) {

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(percentageProtein, "Protein"));
        entries.add(new PieEntry(percentageCarbs, "Carbohydrates"));
        entries.add(new PieEntry(percentageFat, "Fat"));

        PieDataSet nutritionBreakdownDataset = new PieDataSet(entries, "");
        nutritionBreakdownDataset.setColors(ColorTemplate.MATERIAL_COLORS);
        nutritionBreakdownDataset.setSelectionShift(0);
        PieData nutritionData = new PieData(nutritionBreakdownDataset);
        if(percentageCarbs < 0.1 && percentageFat < 0.1 || percentageCarbs < 0.1 && percentageProtein < 0.1
                || percentageFat < 0.1 && percentageProtein < 0.1) {
            nutritionData.setDrawValues(false);
        }
        nutritionData.setValueTextColor(Color.WHITE);
        nutritionData.setValueTextSize(16);
        nutritionData.setValueFormatter(new PercentFormatter());
        chart.setDrawEntryLabels(false);
        chart.getDescription().setEnabled(false);
        chart.setHoleRadius(20f);
        chart.getLegend().setTextSize(16);
        chart.getLegend().setFormSize(16);
        chart.getLegend().setWordWrapEnabled(true);
        chart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        chart.setEntryLabelColor(Color.WHITE);
        chart.setTransparentCircleRadius(0);
        chart.setUsePercentValues(true);
        chart.setTouchEnabled(false);
        chart.setData(nutritionData);
        chart.invalidate();

    }

}
