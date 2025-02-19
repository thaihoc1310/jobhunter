package vn.thaihoc.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.thaihoc.jobhunter.service.EmailService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("api/v1/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("")
    @ApiMessage("Send email")
    public String sendEmail() {
        this.emailService.sendEmailFromTemplateSync("thaihoc131005@gmail.com,", "Test email", "job");
        return "Email sent";
    }
}
