package com.kotlin.vbel.codehunter;

import android.app.Application;
import com.algorithmia.Algorithmia;
import com.algorithmia.AlgorithmiaClient;

public class App extends Application {

    public static AlgorithmiaClient algorithmiaClient;

    @Override
    public void onCreate() {
        super.onCreate();
        algorithmiaClient = Algorithmia.client("simHuy2KeDChHkrT9d6sCPeyZ/b1");
    }
}
