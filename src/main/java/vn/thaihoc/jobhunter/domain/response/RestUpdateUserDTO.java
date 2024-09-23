package vn.thaihoc.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.thaihoc.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class RestUpdateUserDTO {
    private long id;
    private String name;
    private int age;
    private String address;
    private GenderEnum gender;
    private Instant updatedAt;
}
