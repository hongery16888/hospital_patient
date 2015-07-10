package iori.hpatient.modules.common;

public class RequestException extends Exception {
    private int statusCode;

    public RequestException(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
