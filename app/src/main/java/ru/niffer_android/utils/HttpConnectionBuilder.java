package ru.niffer_android.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import net.openid.appauth.Preconditions;
import net.openid.appauth.connectivity.ConnectionBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class HttpConnectionBuilder implements ConnectionBuilder {

    public static final HttpConnectionBuilder INSTANCE = new HttpConnectionBuilder();

    private static final int CONNECTION_TIMEOUT_MS = (int) TimeUnit.SECONDS.toMillis(15);
    private static final int READ_TIMEOUT_MS = (int) TimeUnit.SECONDS.toMillis(10);

    private static final String HTTP_SCHEME = "http";

    private HttpConnectionBuilder() {
        // no need to construct instances of this type
    }

    @NonNull
    @Override
    public HttpURLConnection openConnection(@NonNull Uri uri) throws IOException {
        Preconditions.checkNotNull(uri, "url must not be null");
        Preconditions.checkArgument(HTTP_SCHEME.equals(uri.getScheme()),
                "only http connections are permitted");
        HttpURLConnection conn = (HttpURLConnection) new URL(uri.toString()).openConnection();
        conn.setConnectTimeout(CONNECTION_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);
        conn.setInstanceFollowRedirects(false);
        return conn;
    }
}
