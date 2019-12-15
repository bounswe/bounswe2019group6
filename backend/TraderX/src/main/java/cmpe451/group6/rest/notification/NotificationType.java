package cmpe451.group6.rest.notification;

public enum NotificationType {

    ALERT_TRANSACTION_SUCCESS,
    ALERT_TRANSACTION_FAIL,
    ALERT_NOTIFY,

    FOLLOW_REQUESTED,
    FOLLOWED,
    FOLLOW_REQUEST_ACCEPTED,
    FOLLOW_REQUEST_DENIED;

    private static final String[] ALERT_HEADERS = new String[]{"alertId","timestamp","code","type","amount","limit","message"};
    private static final String[] FOLLOW_HEADERS = new String[]{"username"};

    static String[] payloadHeaders(NotificationType type){
        switch (type) {
            case ALERT_TRANSACTION_SUCCESS:
            case ALERT_TRANSACTION_FAIL:
            case ALERT_NOTIFY:
                return ALERT_HEADERS;
            case FOLLOW_REQUESTED:
            case FOLLOWED:
            case FOLLOW_REQUEST_ACCEPTED:
            case FOLLOW_REQUEST_DENIED:
                return FOLLOW_HEADERS;
        }
        return new String[]{};
    }

}
