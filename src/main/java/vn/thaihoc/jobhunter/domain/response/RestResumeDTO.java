package vn.thaihoc.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.thaihoc.jobhunter.util.constant.StatusEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResumeDTO {
    private long id;
    private String email;
    private String url;
    private StatusEnum status;
    private String companyName;
    private UserResume user;
    private JobResume job;
    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResume {
        private long id;
        private String name;
    }

}
