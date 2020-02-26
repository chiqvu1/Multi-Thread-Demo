package com.example.multithread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    //private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data data = new Data();

                InsertRunnable insertRunnable = new InsertRunnable(data);
                Thread insertThread = new Thread(insertRunnable, "insertThread");

                insertThread.start();

                ProcessRunnable processRunnable1 = new ProcessRunnable(data);
                ProcessRunnable processRunnabble2 = new ProcessRunnable(data);

                Thread processThread1 = new Thread(processRunnable1, "processThread1");

                processThread1.start();

                Thread processThread2 = new Thread(processRunnabble2, "processThread2");

                processThread2.start();
            }
        });

    }
}

class InsertRunnable implements Runnable {
    Data data;
    public InsertRunnable(Data data) {
        this.data = data;
    }

    public void run() {
        synchronized(data) {
            int i = 0;
            while (i < 2000) {
                data.setData();
                i++;
            }
        }
    }
}

class ProcessRunnable implements Runnable {
    Data data;
    public ProcessRunnable(Data data) {
        this.data = data;
    }
    public void run() {
        try {
            while(!data.getData().isEmpty()) {
                int x = data.pullNum();
                System.out.println(Thread.currentThread().getName() + " -----Process number: " + x +
                                    " -----Divisible by 7: " + data.isDivisible(x));
            }


            //data.notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Data {
    private ArrayList<Integer> list;

    public Data() {
        list = new ArrayList();
    }

    public void setData() {
        list.add(new Random().nextInt(10000));
    }

    public ArrayList<Integer> getData() {
        return list;
    }

    public int pullNum() throws InterruptedException {
        int num = list.remove(0);
        return num;
    }

    public boolean isDivisible(int num) {
        boolean flag;
        if (num % 7 == 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
}

