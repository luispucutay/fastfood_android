package com.hcpt.fastfood.network;


import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.hcpt.fastfood.volley.ProgressDialog;

import java.util.Map;

public abstract class HttpRequest {
    public static final int SOCKET_TIME_OUT = 20000;
    public static final int METHOD_GET = Request.Method.GET;
    public static final int METHOD_POST = Request.Method.POST;
    public static final int REQUEST_STRING_PARAMS = 0;
    public static final int REQUEST_JSON_PARAMS = 1;

    protected String url;
    protected HttpListener httpListener;
    protected HttpError httpError;
    protected Map<String, String> params;
    protected int requestDataType;
    private Context context;
    protected int requestMethod;
    protected ProgressDialog pDialog;
    protected boolean isShowDialog = true;
    private RetryPolicy policy = null;
    protected Request request;

    public HttpRequest(Context context, int requestMethod, String url, boolean isShowDialog, HttpListener httpListener, HttpError httpError) {
        this.context = context;
        this.httpListener = httpListener;
        this.httpError = httpError;
        this.requestMethod = requestMethod;
        this.url = url;
        this.isShowDialog = isShowDialog;
        pDialog = new ProgressDialog(context);
        policy = new DefaultRetryPolicy(SOCKET_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    }

    protected void sendRequest()
    {
        request.setRetryPolicy(policy);
        ControllerRequest.getInstance().addToRequestQueue(request);
    }

    protected void showDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(this.context);
        }
        pDialog.show();
    }

    protected void closeDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
        }
    }
}
