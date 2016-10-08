package com.unidev.polydata.storage.flatfile;

import com.unidev.core.okhttp.OkHttpUtils;
import com.unidev.polydata.FlatFileStorage;
import com.unidev.polydata.FlatFileStorageMapper;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.domain.Poly;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Flat file url storage which consider each index record as "page" of other records
 */
public class PagedFlatFileURLStorage {

    public static final String INDEX_FILE = "index.json";
    public static final Integer REQUEST_TIMEOUT = 30 * 1000;

    private Integer page = 0;
    private Map<Integer, FlatFileStorage> pageCache = new ConcurrentHashMap<>();

    private String url;
    private FlatFileStorage index = null;

    private OkHttpClient httpClient;

    public PagedFlatFileURLStorage(String url) {
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
        index = fetchStorage(INDEX_FILE);
        return index;
    }

    /**
     * Fetch poly storage from path
     * @param path Link to storage poly
     * @return
     */
    public FlatFileStorage fetchStorage(String path) {
        Request storageRequest = new Request.Builder()
                .url(url + "/" + path).build();

        Response response;
        ResponseBody responseBody = null;
        FlatFileStorage storage = null;
        try {
            response = httpClient.newCall(storageRequest).execute();
            responseBody = response.body();
            storage = FlatFileStorageMapper.storageMapper().loadSource(responseBody.byteStream()).load();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
        }
        return storage;
    }

    // Navigation related things

    /**
     * Fetch current page polys
     * @return
     */
    public FlatFileStorage fetchPage(Integer page) {
        if (pageCache.containsKey(page)) {
            return pageCache.get(page);
        }
        BasicPoly poly = index.getList().get(page);
        FlatFileStorage pageStorage = fetchStorage(poly.link());
        pageCache.put(page, pageStorage);
        return pageStorage;
    }


    public FlatFileStorage fetchCurrentPage() {
        return fetchPage(page);
    }

    /**
     * Navigate to next page and fetch next page, if it is at last page, will remain at last page
     * @return
     */
    public FlatFileStorage next() {
        if (page == index().list().size() - 1) {
            return fetchCurrentPage();
        }
        page++;
        return fetchCurrentPage();
    }

    /**
     * Navigate to previous page, if it is at first page, will remain on first page
     * @return
     */
    public FlatFileStorage back() {
        if (page == 0) {
            return fetchCurrentPage();
        }
        page--;
        return fetchCurrentPage();

    }

    // Field accessors

    public FlatFileStorage index() {
        return index;
    }

    public Integer page() {
        return page;
    }

    public PagedFlatFileURLStorage page(Integer page) {
        this.page = page;
        return this;
    }

    public PagedFlatFileURLStorage flushCache() {
        pageCache.clear();
        return this;
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
