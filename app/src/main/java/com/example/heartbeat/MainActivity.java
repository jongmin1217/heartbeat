package com.example.heartbeat;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences userData;

    @BindView(R.id.LineChart)
    LineChart lineChart;
    @BindView(R.id.btn_start)
    Button btnStart;

    int num =0;
    boolean high = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // 유저가 처음에 로그인했는지 확인 (안했으면 로그인 activity로 이동)
        userData = getSharedPreferences("userData", 0);
        if (userData.getString("name", "").equals("")) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        //무조건 실행 시켜줘야함
        setChart();

        // 버튼에 클릭이벤트를 사용하겠다
        btnStart.setOnClickListener(this);


    }


    private void setChart() {
        lineChart.setDrawGridBackground(true);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setGridBackgroundColor(Color.WHITE);


        lineChart.getDescription().setEnabled(true);
        Description des = new Description();
        des.setEnabled(true);
        des.setText("심박수 측정");
        des.setTextSize(10f);
        des.setTextColor(R.color.text);
        lineChart.setDescription(des);

        lineChart.setTouchEnabled(false);

        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);

        lineChart.setAutoScaleMinMaxEnabled(true);

        lineChart.setPinchZoom(false);

        lineChart.getXAxis().setDrawGridLines(true);
        lineChart.getXAxis().setDrawAxisLine(false);

        lineChart.getXAxis().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);

        Legend l = lineChart.getLegend();
        l.setEnabled(true);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setTextSize(12f);
        l.setTextColor(Color.WHITE);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.colorPrimaryDark));

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart.invalidate();

    }

    private void addEntry(double num) {

        LineData data = lineChart.getData();

        if (data == null) {
            data = new LineData();
            lineChart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }


        data.addEntry(new Entry((float) set.getEntryCount(), (float) num), 0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        lineChart.notifyDataSetChanged();

        // 그래프에 x축 데이터 갯수
        lineChart.setVisibleXRangeMaximum(60000);
        // this automatically refreshes the chart (calls invalidate())
        lineChart.moveViewTo(data.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);

    }

    private LineDataSet createSet() {


        LineDataSet set = new LineDataSet(null, "심박수 측정");
        set.setLineWidth(1f);
        set.setDrawValues(true);
        set.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        set.setColor(getResources().getColor(R.color.text));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }

    // 버튼 클릭시 실행
    @Override
    public void onClick(View view) {

        //view 보이거나 안보이게하는거
        // INVISIBLE = 안보이는 VISIBLE = 보이는거 GONE = 할당자리까지 없어짐
        btnStart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i<=60000; i++) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(high){
                                num+=120;
                            }else{
                                num-=120;
                            }

                            if(num==30000){
                                high = false;
                            }else if(num==-30000){
                                high = true;
                            }
                            //이게 그래프 출력하는 메소드
                            addEntry(num);
                        }
                    });
                    try {
                        //딜레이
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
