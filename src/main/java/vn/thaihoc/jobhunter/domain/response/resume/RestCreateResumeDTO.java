package vn.thaihoc.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestCreateResumeDTO {
    private long id;

    private Instant createdAt;

    private String createdBy;
}
