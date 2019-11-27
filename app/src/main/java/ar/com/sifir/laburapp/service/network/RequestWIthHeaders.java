package ar.com.sifir.laburapp.service.network;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class RequestWIthHeaders<T> extends JsonRequest<HttpResponse<T>> {

    private final Class<T> type;

    public RequestWIthHeaders(Class<T> type, int method, String url, @Nullable String requestBody, Response.Listener<HttpResponse<T>> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.type = type;
    }

    @Override
    protected Response<HttpResponse<T>> parseNetworkResponse(NetworkResponse response) {
        Gson gson = new Gson();
        try {
            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(
                    new HttpResponse<>(
                            response.headers,
                            gson.fromJson(jsonString, type)
                    ),
                    HttpHeaderParser.parseCacheHeaders(response)
            );
        } catch (UnsupportedEncodingException e) {
            return com.android.volley.Response.error(new ParseError(e));
        }
    }
}
