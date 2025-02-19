package vn.thaihoc.jobhunter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.thaihoc.jobhunter.service.EmailService;
import vn.thaihoc.jobhunter.service.SubscriberService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("api/v1/email")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("")
    @ApiMessage("Send email")
    @Scheduled(cron = "*/30 * * * * *")
    public String sendEmail() {
        this.subscriberService.sendSubscribersEmailJobs();
        return "Email sent";
    }
}
