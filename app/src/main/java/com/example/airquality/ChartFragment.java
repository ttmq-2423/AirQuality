package com.example.airquality;

import static android.text.TextUtils.substring;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.airquality.API.APIInterface;
import com.example.airquality.API.ApIClient;
import com.example.airquality.Model.AssetID;
import com.example.airquality.Model.Body_request_chart;
import com.example.airquality.Model.DataPointChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartFragment extends Fragment {

    ImageButton bt_back;
    ImageButton selectEnding;
    APIInterface apiDatapoint;
    APIInterface apiAssetID;
    LocalDateTime localDateTime;
    String[] item_atribute = {"humidity",  "rainfall", "temperature", "windSpeed"};
    String[] item_timeframe = {"Hour",  "Day", "Week", "Month", "Year"};
    AutoCompleteTextView autoCompleteAttribute;
    AutoCompleteTextView autoCompleteEnding;
    AutoCompleteTextView autoCompleteTimeFrame;
    ArrayAdapter<String>  adapterItem_attribute;
    ArrayAdapter<String>  adapterItem_TimeFrame;
    String attributeName="";
    String timeFrame="";
    long fromTimestamp;
    long toTimestamp;
    int  amountOfPoints = 100;
    String type;

    LineChart lineChart;

    DataPointChart[] dataPoints;
    Button buttonShow;
    String token;
    String assetID;
    String dt;
    long endingDateTime;

    public static ChartFragment newInstance() {
        return new ChartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        lineChart = view.findViewById(R.id.chart);


        lineChart.setVisibility(View.INVISIBLE);

        Bundle bundle = getArguments();
        String User = bundle.getString("user");
        assetID = bundle.getString("assetid");
        token = bundle.getString("token");


        endingDateTime = System.currentTimeMillis();

        autoCompleteAttribute = view.findViewById(R.id.attribute);
        adapterItem_attribute = new ArrayAdapter<String>(getActivity(), R.layout.list_item,item_atribute);
        autoCompleteAttribute.setAdapter(adapterItem_attribute);
        autoCompleteAttribute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                attributeName = parent.getItemAtPosition(position).toString();
            }
        });
        autoCompleteEnding = view.findViewById(R.id.ending);
        autoCompleteTimeFrame = view.findViewById(R.id.time);
        adapterItem_TimeFrame = new ArrayAdapter<String>(getActivity(), R.layout.list_item,item_timeframe);
        autoCompleteTimeFrame.setAdapter(adapterItem_TimeFrame);
        autoCompleteTimeFrame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timeFrame = parent.getItemAtPosition(position).toString();
            }
        });

        localDateTime = convertTimeStampTODatetime(endingDateTime);
        setEndingText(localDateTime);

        selectEnding = view.findViewById(R.id.select_datetime);
        selectEnding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(localDateTime.getYear(),localDateTime.getMonth().getValue()-1,localDateTime.getDayOfMonth());

                int year = calendar.get(calendar.YEAR);
                int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        localDateTime = localDateTime.withYear(year);
                        localDateTime = localDateTime.withMonth(month+1);
                        localDateTime = localDateTime.withDayOfMonth(dayOfMonth);
                        calendar.set(Calendar.MINUTE,localDateTime.getMinute());
                        calendar.set(Calendar.HOUR,localDateTime.getHour());
                        int minute = calendar.get(Calendar.MINUTE);
                        int hour = calendar.get(Calendar.HOUR);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                localDateTime = localDateTime.withMinute(minute);
                                localDateTime = localDateTime.withHour(hourOfDay);
                                endingDateTime = convertDateTimeTOTimestamp(localDateTime);
                                setEndingText(localDateTime);
                            }
                        }, hour, minute, true);
                        timePickerDialog.show();

                    }
                }, year, month,day);
                datePickerDialog.show();



            }
        });

        buttonShow =view.findViewById(R.id.btnshow);
        buttonShow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CallDataPoints(assetID);
            }
        });

    return view;
    }
    public void CallDataPoints(String AssetID) {

        type = "lttb";
        int amountOfPoints = 100;
        assetID = AssetID;

        toTimestamp = endingDateTime;
        localDateTime = convertTimeStampTODatetime(toTimestamp);

        switch (timeFrame)
        {
            case "Hour":
                fromTimestamp =convertDateTimeTOTimestamp(localDateTime.minusHours(1));
                break;
            case "Day":
                fromTimestamp = convertDateTimeTOTimestamp(localDateTime.minusDays(1));
                break;
            case "Month":
                fromTimestamp = convertDateTimeTOTimestamp(localDateTime.minusMonths(1));
                break;
            case "Week":
                fromTimestamp = convertDateTimeTOTimestamp(localDateTime.minusWeeks(2));
                break;
            case "Year":
                fromTimestamp = convertDateTimeTOTimestamp(localDateTime.minusYears(1));
                break;
        }
        Body_request_chart bodyRequestChart = new Body_request_chart(type, fromTimestamp, toTimestamp, amountOfPoints);
        apiDatapoint = ApIClient.setToken(token);
        apiDatapoint = ApIClient.getClient().create(APIInterface.class);
        Call<DataPointChart[]> call = apiDatapoint.postDatapoint(assetID, attributeName, bodyRequestChart);
        call.enqueue(new Callback<DataPointChart[]>() {
            @Override
            public void onResponse(Call<DataPointChart[]> call, Response<DataPointChart[]> response) {
                Log.d("API CALL", response.code() + "");
                Log.d("API CALL", response.headers() + "");
                Log.d("API CALL", response.body() + "");
                dataPoints = response.body();
                DrawChart();
                String data = "";
            }
            @Override
            public void onFailure(Call<DataPointChart[]> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString());
            }
        });
    }
    public void DrawChart()
    {
        long MaxX = 0;
        long MinX = 0;
        Float MaxY = 0f;
        lineChart.setVisibility(View.VISIBLE);

        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawMarkers(false);
        lineChart.setHighlightPerTapEnabled(false);

        localDateTime = localDateTime.withSecond(0);
        localDateTime = localDateTime.plusMinutes(1);
        toTimestamp = convertDateTimeTOTimestamp(localDateTime);


        if (dataPoints.length == 0)
        {
            LineData lineData = new LineData();
            lineChart.setData(lineData);
            lineChart.invalidate();
        }
        else {
            XAxis xAxis = lineChart.getXAxis();
            YAxis yAxis = lineChart.getAxisLeft();
            MaxX = toTimestamp;
            LocalDateTime localDateMaxX = convertTimeStampTODatetime(MaxX);

            for (int i = 0; i < dataPoints.length; i++) {
                if (MaxY < dataPoints[i].y) {
                    MaxY = dataPoints[i].y;
                }
            }
            if (MaxY > Math.round(MaxY)) {
                MaxY = Math.round(MaxY) + 1.0f;
            }
            final List<String> xLabel = new ArrayList<>();
            xAxis.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(45f);

            switch (timeFrame) {
                case "Hour":
                    xAxis.setLabelCount(13);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                            LocalDateTime localDateTime = convertTimeStampTODatetime((long) value);
                            return localDateTime.getHour() + ":" + localDateTime.getMinute();
                        }
                    });
                    break;
                case "Day":
                    xAxis.setLabelCount(25);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                            LocalDateTime localDateTime = convertTimeStampTODatetime((long) value);
                            return localDateTime.getHour() + ":" + localDateTime.getMinute();
                        }
                    });
                    ;
                    break;
                case "Week":
                    xAxis.setLabelCount(15);
                    MaxX = convertDateTimeTOTimestamp(localDateMaxX);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                            LocalDateTime localDateTime = convertTimeStampTODatetime((long) value);
                            return localDateTime.getHour() + ":" + localDateTime.getMinute();
                        }
                    });
                    break;
                case "Month":
                    xAxis.setLabelCount(16);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                            LocalDateTime localDateTime = convertTimeStampTODatetime((long) value);
                            return localDateTime.getMonth() + " " + localDateTime.getDayOfMonth();
                        }
                    });
                    break;
                case "Year":
                    xAxis.setLabelCount(13);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                            LocalDateTime localDateTime = convertTimeStampTODatetime((long) value);
                            return localDateTime.getYear() + " " + localDateTime.getMonth();
                        }
                    });
                    break;
            }
            //yAxis.setAxisMinimum(0f);
            xAxis.setAxisMinimum(dataPoints[0].x);
            xAxis.setAxisMaximum(dataPoints[dataPoints.length-1].x);

            yAxis.setAxisMaximum(MaxY);
            yAxis.setAxisLineColor(Color.BLACK);

            List<Entry> entries1 = new ArrayList<>();
            for (int i = 0; i < dataPoints.length; i++)
                entries1.add(new Entry(dataPoints[i].x, dataPoints[i].y));
            LineDataSet dataSet1 = new LineDataSet(entries1, attributeName);
            dataSet1.setDrawFilled(true);
            dataSet1.setColor(Color.BLUE);
            dataSet1.setCircleRadius(5f);
            dataSet1.setCircleColor(Color.BLUE);
            dataSet1.setLineWidth(2f);
            //dataSet1.getCircleColors(Color.BLUE);
            dataSet1.setFillColor(Color.BLUE);
            dataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            LineData lineData = new LineData(dataSet1);
            lineChart.setData(lineData);
            lineChart.setPinchZoom(false);
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setSelected(true);
            lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    float xValue = e.getX();
                    float yValue = e.getY();

                   // Toast.makeText(getApplicationContext(), "Selected: x = " + convertTimeStampTODatetime((long)xValue) + ", y = " + yValue, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected() {
                }
            });

            lineChart.invalidate();
        }

    }
    public LocalDateTime convertTimeStampTODatetime(long timestamp)
    {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime;
    }
    public long convertDateTimeTOTimestamp(LocalDateTime localDateTime)
    {

        long timestamp = localDateTime.atZone(ZoneId.of("UTC")).toEpochSecond();
        return timestamp*1000;
    }
    public void setEndingText(LocalDateTime localDateTime)
    {
        String month = String.valueOf(localDateTime.getMonth());
        month = substring(month,0, 3);
        autoCompleteEnding.setText( month + " "  +localDateTime.getDayOfMonth()+ ","
                + localDateTime.getYear()+" " +
                String.format("%02d", localDateTime.getHour()) + ":"+
                String.format("%02d", localDateTime.getMinute()));
    }




}