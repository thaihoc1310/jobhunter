package vn.thaihoc.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.thaihoc.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class RestCreateUserDTO {
    private long id;
    private String email;
    private String name;
    private int age;
    private String address;
    private GenderEnum gender;
    private Instant createdAt;
    private CompanyUser company;

    @Getter
    @Setter
    public class CompanyUser {
        private long id;
        private String name;
    }
}
