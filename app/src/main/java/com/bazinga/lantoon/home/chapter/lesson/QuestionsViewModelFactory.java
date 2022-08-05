package com.bazinga.lantoon.home.chapter.lesson;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class QuestionsViewModelFactory implements ViewModelProvider.Factory {
    boolean isEvaluation;
    int langid, chaperno, lessonno, knownLang, chapterType;

    public QuestionsViewModelFactory(boolean isEvaluation,int langid, int chaperno, int lessonno, int knownLang,int chapterType) {
        this.isEvaluation = isEvaluation;
        this.langid = langid;
        this.chaperno = chaperno;
        this.lessonno = lessonno;
        this.knownLang = knownLang;
        this.chapterType = chapterType;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new QuestionsViewModel(isEvaluation,langid,chaperno,lessonno,knownLang,chapterType);
    }
}
