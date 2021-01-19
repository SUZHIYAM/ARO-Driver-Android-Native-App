package com.tn74travellers.aroDriver.Common;

public class CommonVariables
{
    /* Common Variables */

    public static final String BLANK_STRING                 = "";
    public static final String YES                          = "Yes";
    public static final String NO                           = "No";
    public static final String POST                         = "POST";
    public static final String PUT                          = "PUT";
    public static final String GET                          = "GET";
    public static final String JSON                         = "json";
    static final String FILE                                = "file";
    public static final String HEADER                       = "header";
    public static final String ACCEPTED                     = "ACCEPTED";
    public static final String NO_HEADER                    = "no_header";
    public static final String URLR_STATUS                  = "status";
    public static final String URLRS_RESPONSE_CODE          = "responseCode";
    public static final String URLRS_RESPONSE_DATA          = "responseData";
    public static final String URLRS_RESPONSE_MSG           = "responseMsg";
    public static final String URLRQ_SUCCESS                = "Success";
    public static final String URLR_MOBILE_NO_NOT_FOUND     = "User Mobile No not found";
    public static final String IMAGE_PATH                   = "image_path";
    public static final String IMAGE_NAME                   = "image_name";
    public static final String PDF_FILE_PATH                = "pdf_file_path";
    public static final String PDF_FILE_NAME                = "pdf_file_name";
    public static final String PDF_FILE_SIZE                = "pdf_file_size";
    public static final String PDF_FILE_SAVED_DATE          = "pdf_file_saved_date";
    public static final String PDF_FILE_SAVED_TIME          = "pdf_file_saved_time";
    public static final String SELECT                       = "Select";
    public static final String SELECT_AND_DELETE            = "Select and Delete";
    public static final String UPLOAD                       = "Upload";
    public static final String DELETE                       = "Delete";
    public static final String LOGOUT_SUCCESSFULLY          = "Logout Successfully";
    public static final String LOGIN_SUCCESSFULLY           = "Login Successfully";
    public static final String SESSION_EXPIRED              = "Login Your Session has expired.Please relogin with your registered Mobile Number";
    public static final String PICKED_UP                    = "pickedup";
    public static final String DROPPED                      = "dropped";
    public static final String FROM                         = "from";
    public static final String FETCHING_LOCATION            = "Fetching Location...";
    public static final String CHANGE_TRIP_STATUS           =  "Change-Trip-Status";


    /*------------- Service Response Codes -----------------------*/

    public static final String SERVICE_RESPONSE_CODE_200            = "200";
    public static final String SERVICE_RESPONSE_CODE_202            = "202";
    public static final String SERVICE_RESPONSE_CODE_404            = "404";
    public static final String SERVICE_RESPONSE_CODE_401            = "401";


    public static final String SP_VERSION_NUMBER                    = "sp_version_number";
    public static final String SP_LOGIN_STATUS                      = "sp_login_status";
    public static final String SP_DRIVER_ID                         = "sp_driver_id";
    public static final String SP_GCM_ID                            = "sp_gcm_id";
    public static final String SP_DEVICE_TOKEN                      = "sp_device_token";
    public static final String SP_NOTIFICATION                      = "sp_notification";
    public static final String SP_PAYMENT_UPDATED                   = "sp_payment_updated";
    public static final String SP_TRAVELS_ID                        = "sp_travels_id";
    public static final String SP_ONGOING_TRIP_ID                   = "sp_on_going_trip_id";


    /*------------Code Patterns----------------------------*/

    public static final String CODE_EMAIL_PATTERN                   = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
    public static final String CODE_ONLY_ALPHABETS_PATTERN          = "^[a-zA-Z ]+$";

    public static final String UNABLE_TO_FIND_PATH                  = "Unable to find image path";
    public static final String UNABLE_TO_FIND_IMAGE_PATH            = "Unable to find image path";
    public static final String UNABLE_TO_CROP                       = "Unable to crop";



    /* Case Variables */

    public static final String CASE_VERSION_NUMBER                  = "case_version_number";
    public static final String CASE_LOGIN                           = "case_login";
    public static final String CASE_NEW_TRIP_LIST                   = "case_new_trip_list";
    public static final String CASE_ONGOING_TRIP_CHECK              = "case_ongoing_trip_check";
    public static final String CASE_NEW_TRIP_LOCATION_LIST          = "case_new_trip_location_list";
    public static final String CASE_TRIP_ACCEPT                     = "case_trip_accept";
    public static final String CASE_TRIP_STATUS_CHANGE              = "case_trip_status_change";
    public static final String CASE_PAYMENT_STATUS_CHANGE           = "case_payment_status_change";
    public static final String CASE_GET_PAYMENT_RESPONSE            = "case_get_payment_response";
    public static final String CASE_UPDATE_PUSH_NOTI                = "case_update_push_noti";
    public static final String CASE_ADD_FEEDBACK                    = "case_add_feedback";
    public static final String CASE_ONGOING_TRIP                    = "case_ongoing_trip";
    public static final String CASE_EVENT_TRIP_LIST                 = "case_event_trip_list";
    public static final String CASE_TRIP_STATUS_LIST                = "case_trip_status_list";
    public static final String CASE_TRIP_DATE_LIST                  = "case_trip_date_list";
    public static final String CASE_PICK_TRIP                       = "case_pick_trip";
    public static final String CASE_GET_IMAGE_URL                   = "case_get_image_url";
    public static final String CASE_IMAGE_URL                       = "case_image_url";
    public static final String CASE_IMAGE_ZOOM_URL_PATH             = "image_zoom_url_path";
    public static final String CASE_IMAGE_ZOOM_IMG_PATH             = "image_zoom_img_path";
    public static final String CASE_IMAGE_ZOOM_RESOURCE_ID          = "image_zoom_resource_id";

    /* Messages */

    public static final String MSG_LOADING_DOTS                 = "Loading...";
    public static final String MSG_NETWORK_UNAVAILABLE          = "Network unavailable";


    /* Error Messages */

    public static final String EM_FILL_FIRST_NAME               = "Please enter your First Name";
    public static final String EM_FILL_LAST_NAME                = "Please enter your Last Name";
    public static final String EM_FILL_ADDRESS                  = "Please enter your Address";
    public static final String EM_FILL_MOBILE_NO                = "Please Enter Your Mobile Number";
    public static final String EM_FILL_CITY                     = "Please enter your City";
    public static final String EM_FILL_ZIPCODE                  = "Please enter your ZipCode";
    public static final String EM_INVALID_FIRST_NAME            = "First name must have only alphabets";
    public static final String EM_INVALID_LAST_NAME             = "Last name must have only alphabets";
    public static final String EM_INVALID_MOBILE_NO             = "Invalid Mobile Number";
    public static final String EM_INVALID_CITY                  = "City must have only alphabets";
    public static final String EM_INVALID_ZIPCODE               = "Invalid ZipCode";
    public static final String EM_INVALID_FIRST_NAME_CHAR       = "First name must have min 3 and max 25 characters";
    public static final String EM_INVALID_LAST_NAME_CHAR        = "Last name must have min 3 and max 20 characters";
    public static final String EM_SORRY_TRY_AGAIN               = "Sorry some error occurred please try again";
    public static final String EM_INVALID_USER                  = "Invalid User";
    public static final String EM_FILE_NAME                     = "Please enter your File Name";
    public static final String EM_FILL_PASSWORD                 = "Please Enter Your Password";
    public static final String EM_INVALID_PASSWORD              = "Password Length Must be Atleast 5";


    /* URL Variables */

    public static final String URLRQ_MOBILE_TYPE                = "mobileType";
    public static final String URLRQ_USER_TYPE                  = "userType";
    public static final String URL_TRIP_ID                      = "trip_id";
    public static final String URL_TRIPSTATUSID                 = "tripStatusId";
    public static final String URL_FIRST_NAME                   = "FirstName";
    public static final String URL_LAST_NAME                    = "LastName";
    public static final String URL_ADDRESS                      = "Address";
    public static final String URL_MOBILE                       = "mobile";
    public static final String URL_PASSWORD                     = "password";
    public static final String URLRQ_DRIVER_ID                  = "driver_id";
    public static final String URLRQ_TRAVELS_ID                 = "travels_id";
    public static final String URLRQ_ACTION_TYPE                = "action_type";
    public static final String URLRQ_DATE                       = "date";
    public static final String URL_CURRENTDATE                  = "currentDate";
    public static final String URL_PASSANGERID                  = "passangerId";
    public static final String URLRQ_DEVICE_TOKEN               = "deviceToken";
    public static final String URLRQ_ANDROID                    = "android";
    public static final String URL_CITY                         = "City";
    public static final String URL_ZIP                          = "Zip";
    public static final String URL_STATUS                       = "status";
    public static final String URL_MESSAGE                      = "message";
    public static final String URL_LAT_LONG                     = "lat_long";

    public static final String URL_EVENT_TYPE                   = "event_type";
    public static final String URL_EVENT_TIME                   = "event_time";
    public static final String URL_CURRENT_LOCATION             = "current_location";

    public static final String URL_TRIP_TYPE                    = "tripType";

    /* Trip Action */

    public static final String URLRQ_TRIP_ONBOARDED                 = "trip_onboarded";
    public static final String URLRQ_TRIP_ACCEPTED                  = "trip_accepted";
    public static final String URLRQ_TRIP_READY_TO_PICK             = "trip_ready_to_pickup";
    public static final String URLRQ_TRIP_STARTED                   = "trip_started";
    public static final String URLRQ_TRIP_CANCELLED                 = "trip_cancelled";
    public static final String URLRQ_TRIP_COMPLETED                 = "trip_completed";
    public static final String URLRQ_TRIP_PAYMENT_COMPLETED         = "trip_payment_completed";

    /*  */

    public static final String TRIP_TYPE_ROUND_TRIP                 = "Round Trip";
    public static final String TRIP_TYPE_ONE_WAY_TRIP               = "One Way Trip";
    public static final String TRIP_TYPE_RENTAL_TRIP                = "rental";
    public static final String TRIP_TYPE_AIRPORT_TRIP               = "airport";


    /* Intent Variables */

    public static final String INTENT_IMAGE_ZOOM_PATH_TYPE      = "image_zoom_path_type";
    public static final String INTENT_IMAGE_ZOOM_IMG_PATH       = "image_zoom_img_path";

    public static final String DOCUMENT_TYPE_NAME_LIST          = "[\n" +
            "\t{\"file_type\":\"CDL\",\"file_name\":\"Driver's licence\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"Invoice\",\"file_name\":\"Invoice Receipt\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"Address proof\",\"file_name\":\"Address Proof\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"DOB\",\"file_name\":\"Date Of Birth\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"Others\",\"file_name\":\"Others\", \"is_selected\" : true},\n" +
            "\t{\"file_type\":\"Shipping Forms\",\"file_name\":\"Shipping Forms\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"BOL\",\"file_name\":\"Bill of Ladding\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"Registration\",\"file_name\":\"Registration\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"Travel Permit\",\"file_name\":\"Travel Permit\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"Inspection\",\"file_name\":\"Inspection\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"Insurance\",\"file_name\":\"Insurance\", \"is_selected\" : false},\n" +
            "\t{\"file_type\":\"Shipping do\",\"file_name\":\"Shipping do\", \"is_selected\" : false}\n" +
            "];";

    public static final String URLE_STATUS                      = "status";
    public static final String URLE_LOCATION                    = "location";
    static final String URLE_CLIENT_ID                          = "client_id";
    static final String URLE_CLIENT_SECRET                      = "client_secret";
    public static final String URLE_V                           = "v";
    public static final String URLE_LL                          = "ll";
    public static final String URLE_NAME                        = "name";
    public static final String URLE_PRODUCT                     = "product";
    public static final String URLE_APP_ID                      = "app_id";
    public static final String URLE_APP_CODE                    = "app_code";

    public static final String URLR_LOAD_ID                     = "loadId";
    public static final String URLR_SHIPPER_NAME                = "shipperName";
    public static final String URLR_CONSIGNEE_NAME              = "consigneeName";
    public static final String URLR_PICK_UP_LOCATION            = "pickupLocation";
    public static final String URLR_DROP_LOCATION               = "dropLocation";
    public static final String URLR_LOAD_STATUS                 = "loadStatus";
    public static final String URLR_ACCEPTED                    = "ACCEPTED";

    public static final String LOCAL_BROADCAST_ACTION_NEW_PUSH_MSG  = "HAS_NEW_PUSH_NOTIFICATION";
    public static final String HAS_NEW_PUSH_MSG                     = "HAS_NEW_PUSH_MSG";

    public static final String NEW_APP_VERSION_DOWNLOAD_MSG         = "This version of ARO became outdated.Tap DOWNLOAD below to get the latest version from the Google Play Store.";
}
