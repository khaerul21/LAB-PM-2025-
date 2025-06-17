package com.example.mylibraryapp.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BookViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public BookViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BookViewModel.class)) {
            return (T) new BookViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
