package vn.thaihoc.jobhunter.domain.response;

import vn.thaihoc.jobhunter.util.constant.LevelEnum;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestUpdateJobDTO {
    private long id;

    private String name;

    private double salary;

    private int quantity;

    private String location;

    private LevelEnum level;

    private Instant startDate;

    private Instant endDate;

    private boolean active;

    private Instant updatedAt;

    private String updatedBy;

    private List<String> skills;

}
