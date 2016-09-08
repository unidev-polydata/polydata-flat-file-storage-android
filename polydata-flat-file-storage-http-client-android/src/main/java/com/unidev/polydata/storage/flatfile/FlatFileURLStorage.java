package com.unidev.polydata.storage.flatfile;

import com.unidev.core.okhttp.OkHttpUtils;
import com.unidev.polydata.FlatFileStorage;
import com.unidev.polydata.FlatFileStorageMapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Loader of  polydata records from remote URL
 * To work properly, provide root url where is stored index
 */
public class FlatFileURLStorage {

    public static final String INDEX_FILE = "index.json";
    public static final Integer REQUEST_TIMEOUT = 30 * 1000;


    private String url;
    private FlatFileStorage index = null;

    private OkHttpClient httpClient;

    public FlatFileURLStorage(String url) {
        this.url = url;

        httpClient = OkHttpUtils.fetchUnsafeOkHttpClient()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Fetch remote index file
     * @return
     */
    public FlatFileStorage fetchIndex() {
        Request indexRequest = new Request.Builder()
                .url(url + "/" + INDEX_FILE).build();
        Response response;
        ResponseBody responseBody = null;
        try {
            response = httpClient.newCall(indexRequest).execute();
            responseBody = response.body();
            index = FlatFileStorageMapper.storageMapper().loadSource(responseBody.byteStream()).load();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
        }
        return index;
    }


    public FlatFileStorage index() {
        return index;
    }

    public String url() {
        return url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
