package vn.thaihoc.jobhunter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.thaihoc.jobhunter.service.SubscriberService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("api/v1/email")
public class EmailController {
    private final SubscriberService subscriberService;

    public EmailController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("")
    @ApiMessage("Send email")
    @Scheduled(cron = "0 0 0 * * MON")
    public String sendEmail() {
        this.subscriberService.sendSubscribersEmailJobs();
        return "Email sent";
    }
}
