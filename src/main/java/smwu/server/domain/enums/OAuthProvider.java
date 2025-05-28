package smwu.server.domain.enums;

public enum OAuthProvider {
    ORIGIN,
    KAKAO;

    public static OAuthProvider fromString(String provider) {
        switch(provider) {
            case "kakao" : return KAKAO;
            default : return ORIGIN;
        }
    }
}
