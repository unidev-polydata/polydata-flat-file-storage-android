package com.unidev.app.testapp.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

public class InitializationOperation extends ChronosOperation<Void> {
    @Nullable
    @Override
    public Void run() {
        try {
            Core.getInstance().load();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Void>> getResultClass() {
        return InitializationOperationResult.class;
    }

    public static final class InitializationOperationResult extends ChronosOperationResult<Void> { }

}