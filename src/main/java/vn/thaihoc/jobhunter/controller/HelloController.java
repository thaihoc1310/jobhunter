package vn.thaihoc.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.thaihoc.jobhunter.domain.HandleString;

@RestController
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<HandleString> getHelloWorld() {
        return ResponseEntity.status(HttpStatus.OK).body(new HandleString("Hello World , this is job-hunter project"));
    }
}
