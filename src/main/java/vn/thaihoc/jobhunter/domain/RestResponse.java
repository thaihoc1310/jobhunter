package vn.thaihoc.jobhunter.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private String error;

    // string or arraylist
    private Object message;
    private T data;

}
