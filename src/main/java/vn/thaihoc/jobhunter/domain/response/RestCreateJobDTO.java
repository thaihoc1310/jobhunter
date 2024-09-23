package vn.thaihoc.jobhunter.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.thaihoc.jobhunter.util.constant.LevelEnum;

import java.time.Instant;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestCreateJobDTO {
    private long id;

    private String name;

    private double salary;

    private int quantity;

    private String location;

    private LevelEnum level;

    private Instant startDate;

    private Instant endDate;

    private boolean active;

    private Instant createdAt;

    private String createdBy;

    private List<String> skills;

}
