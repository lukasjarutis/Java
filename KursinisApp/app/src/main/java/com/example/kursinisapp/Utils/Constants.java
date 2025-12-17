package com.example.kursinisapp.Utils;

public class Constants {
    public static final String HOME_URL = "http://192.168.1.162:8080/";
    public static final String API_URL = HOME_URL + "api/";

    public static final String VALIDATE_USER_URL = API_URL + "auth/login";
    public static final String GET_ALL_RESTAURANTS_URL = API_URL + "restaurants";
    public static final String CREATE_BASIC_USER_URL = HOME_URL + "insertBasic";
    public static final String GET_ORDERS_BY_USER = API_URL + "orders";
    public static final String GET_MESSAGES_BY_ORDER = API_URL + "orders/";
    public static final String SEND_MESSAGE = HOME_URL + "sendMessage";
    public static final String GET_RESTAURANT_MENU = API_URL + "restaurants/";
    public static final String CREATE_ORDER = API_URL + "orders";
    public static final String UPDATE_ORDER_STATUS = API_URL + "orders/";
    public static final String GET_USER_INFO = API_URL + "users/";
    public static final String UPDATE_USER_INFO = API_URL + "users/";
}
