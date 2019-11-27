package ar.com.sifir.laburapp.service.network;

import java.util.Map;

public class HttpResponse<T> {
    private Map<String, String> headers;
    private T body;

    HttpResponse(Map<String, String> headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }
}