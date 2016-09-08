/**
 * Copyright (c) 2015,2016 Denis O <denis@universal-development.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unidev.app.testapp.core;

import android.content.Context;

import com.unidev.core.di.AppContext;
import com.unidev.polydata.storage.flatfile.FlatFileURLStorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application custom backend logic
 */
public class Core {

    public static final String INSTANCE_NAME = "core";

    public static Core getInstance() {
        return AppContext.getInstance(INSTANCE_NAME, Core.class);
    }

    private FlatFileURLStorage flatFileURLStorage;

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void load(Context context) {
        flatFileURLStorage = new FlatFileURLStorage("http://10.10.10.11:8000");
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                flatFileURLStorage.fetchIndex();
            }
        });
    }

    public FlatFileURLStorage flatFileURLStorage() {
        return flatFileURLStorage;
    }

}
