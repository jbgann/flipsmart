//Taken from Medium article at https://medium.com/@ssaurel/learn-to-create-a-paint-application-for-android-5b16968063f8

package com.example.flipsmart;

import android.graphics.Path;

public class FingerPath {

    public int color;
    public boolean emboss;
    public boolean blur;
    public int strokeWidth;
    public Path path;

    public FingerPath(int color, boolean emboss, boolean blur, int strokeWidth, Path path) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}
