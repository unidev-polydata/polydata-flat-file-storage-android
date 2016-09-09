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
package com.unidev.app.testapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.unidev.app.testapp.core.InitializationOperation;
import com.unidev.uiutils.AbstractActivity;
import com.unidev.uiutils.UIUtils;

public class MainActivity extends AbstractActivity {

    public static final String LOADING_TAG = "loadingTag";


    @Override
    protected boolean enableInfiniteLoading() {
        return false;
    }


    @Override
    public Fragment buildContentFragment() {
            return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runOperation(new InitializationOperation(), LOADING_TAG);
    }

    public void onOperationFinished(final InitializationOperation.InitializationOperationResult result) {
        Fragment fragment = new MainFragment();
        replaceContent(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
