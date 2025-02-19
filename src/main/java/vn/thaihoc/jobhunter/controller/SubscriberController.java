package vn.thaihoc.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.Subscriber;
import vn.thaihoc.jobhunter.service.SubscriberService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.EmailInvalidException;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("api/v1/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createnewSubscriber(@Valid @RequestBody Subscriber subscriber)
            throws EmailInvalidException {
        Subscriber newsubscriber = this.subscriberService.handleCreateSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(newsubscriber);
    }

    @PutMapping("")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber)
            throws IdInvalidException {
        Subscriber updatedSubscriber = this.subscriberService.handleUpdateSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSubscriber);
    }

}
