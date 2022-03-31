package xyz.fm.storerestapi.error;

public class ErrorDetail {

    public static final String NOT_BLANK = "필수항목이며, 공백이 아닌 최소 1 글자 이상이어야 합니다.";
    public static final String NOT_NULL = "필수항목입니다.";
    public static final String NOT_EMAIL_FORMAT = "이메일 형식이어야 합니다.";
    public static final String NOT_PHONE_NUMBER_FORMAT = "휴대폰 번호 형식이어야 합니다.(ex. 01x-xxxx-xxxx or 01xxxxxxxxx)";
    public static final String DUPLICATE_USER = "이미 등록된 사용자입니다.";

    /* 확인용 메시지 */
    public static final String NOT_CONSUMER_JOIN_REQUEST = "Argument 타입이 ConsumerJoinRequest 가 아닙니다.";
}
