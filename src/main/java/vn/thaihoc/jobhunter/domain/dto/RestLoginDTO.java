package vn.thaihoc.jobhunter.domain.dto;

public class RestLoginDTO {
    public String accessToken;

    public RestLoginDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
