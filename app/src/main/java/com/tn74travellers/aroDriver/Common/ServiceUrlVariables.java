package com.tn74travellers.aroDriver.Common;

public class ServiceUrlVariables
{
    private static final String DOMAIN_NAME                 = "https://www.arotravellers.com";

    private static final String API                         = "/api";
    
    public static final String VERSION_NUMBER               = DOMAIN_NAME+API+"/app-version";

    public static final String LOGIN                        = DOMAIN_NAME+API+"/driver-login";

    public static final String LOGOUT                       = DOMAIN_NAME+API+"/driver-logout";

    public static final String PROFILE                      = DOMAIN_NAME+API+"/driver-profile";

    public static final String UPDATE_PUSH_NOTI             = DOMAIN_NAME+API+"/driver-fcm";

    public static final String GET_NEW_TRIP_LIST            = DOMAIN_NAME+API+"/trip-list";

    public static final String GET_TRIP_DATE                = DOMAIN_NAME+API+"/trip-calendar";

    public static final String GET_ONGOING_TRIP_CHECK       = DOMAIN_NAME+API+"/trip-ongoing";

    public static final String GET_TRIP_DAY_LIST            = DOMAIN_NAME+API+"/trip-day";

    public static final String TRIP_ACTION                  = DOMAIN_NAME+API+"/trip-action";

    public static final String TRIP_ONGOING_DETAILS         = DOMAIN_NAME+API+"/trip-ongoing-details";

    public static final String TRIP_STATUS_CHANGE           = DOMAIN_NAME+API+"/tripStatusChange";

    //public static final String ONGOING_TRIP                 = DOMAIN_NAME+API+"/OnGoingtripDetailsForDriver";

    public static final String GET_IMAGE_URL                = DOMAIN_NAME+API+"/getImage";

    public static final String TRIP_STATUS_LIST             = DOMAIN_NAME+API+"/tripStatusList";

    //public static final String TRIP_PICK                    = DOMAIN_NAME+API+"/pickedUpConfirmation";

    public static final String PAYMENT_STATUS_CHANGE        = DOMAIN_NAME+API+"/paymentStatusChange";

    public static final String PAYMENT_UPDATE               = DOMAIN_NAME+API+"/paymentStatusChange";

    public static final String GET_PAYMENT_RESPONSE         = DOMAIN_NAME+API+"/getPaymentResponse";

    public static final String ADD_RATING                   = DOMAIN_NAME+API+"/trip-rating-by-driver";

}
