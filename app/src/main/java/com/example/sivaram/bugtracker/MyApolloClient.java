package com.example.sivaram.bugtracker;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApolloClient {
    private  static final String BASE_URL = "https://bugtracker-server.herokuapp.com/";
    private static ApolloClient apolloClient;


    public static ApolloClient getApolloClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        apolloClient=ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(okHttpClient).build();
        return apolloClient;
    }
}
