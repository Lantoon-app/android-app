package com.bazinga.lantoon.home.chapter.lesson;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TaskState.CREATED,TaskState.WAITING, TaskState.RUNNING, TaskState.COMPLETED, TaskState.CANCELLED, TaskState.STOP})
@Retention(RetentionPolicy.SOURCE)
public @interface TaskState {
    public static final int CREATED = 0;
    public static final int WAITING = 1;
    public static final int RUNNING = 2;
    public static final int COMPLETED = 3;
    public static final int CANCELLED = 4;
    public static final int STOP = 5;
}