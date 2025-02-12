package vn.thaihoc.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestUpdateResumeDTO {
    private Instant updatedAt;

    private String updatedBy;
}
