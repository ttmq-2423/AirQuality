package com.example.airquality.API;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApIClient {
    private static Retrofit retrofit =null;

   public static APIInterface setToken(String token) {
        ApIClient.token = token;
        return null;
    }
    private static String token = null;
    //private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDIxMDY2MzYsImlhdCI6MTcwMjAyMDc3NywiYXV0aF90aW1lIjoxNzAyMDIwMjM2LCJqdGkiOiJiNmExZDViZS1iNmU1LTRjYTktOTZlNC1jOWNlM2Y1ZjJjMTgiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiZjBmMmUwZGYtOThiYy00MzYwLWIxNTAtNDU3MjM1NWY5MDJmIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJmMGYyZTBkZi05OGJjLTQzNjAtYjE1MC00NTcyMzU1ZjkwMmYiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJGaXJzdCBOYW1lIExhc3QgbmFtZSIsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIiLCJnaXZlbl9uYW1lIjoiRmlyc3QgTmFtZSIsImZhbWlseV9uYW1lIjoiTGFzdCBuYW1lIiwiZW1haWwiOiJ1c2VyQGl4eGMuZGV2In0.BXgV30kGSeE3CyubtN7bPnK738qDd5DnCGNWmwDgsj1QI20o7DNp5e4Zg86xbb6tOW_Hf_b-zkZVW3W9xOpAIA0K4xw578h78ge4_wt7zAfhuSLtfDQSxoyvfo0ETk_Nj5dVFfGd2CnntUWazWE_fI_qSQ9Jz5H1uOB_3YH_DCEda4oKa_wB0W-MmXqUfIqvtYllV2x5roAjhzZovflPIX6pJwvl5lGE4BaTpTyH3nMnnpnCGPYfgXN1AIn_PMq8y00TTR9mUN870JZ0CmrLsUFguwm85keeAy_QoCfuEhZ_F0WPijbx6Ay7GzeStSyshBILaBW7FyTENeGOTvf4Aw";
    private static OkHttpClient getUnsafeOkHttpClient(){
       try{
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("accept", "application/json")
                            .addHeader("Authorization", "Bearer " + token)
                            .addHeader("Content-Type","application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            });


            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Retrofit getClient() {
        OkHttpClient client = getUnsafeOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
