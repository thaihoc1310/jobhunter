package vn.thaihoc.jobhunter.domain.dto;

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
}
