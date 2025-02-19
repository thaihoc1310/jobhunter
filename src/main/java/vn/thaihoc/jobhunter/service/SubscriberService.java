package vn.thaihoc.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Subscriber;
import vn.thaihoc.jobhunter.repository.SubscriberRepository;
import vn.thaihoc.jobhunter.util.error.EmailInvalidException;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillService skillService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillService skillService) {
        this.subscriberRepository = subscriberRepository;
        this.skillService = skillService;
    }

    public boolean isSubscriberExist(Subscriber subscriber) {
        return this.subscriberRepository.existsByEmail(subscriber.getEmail());
    }

    public Subscriber handleCreateSubscriber(Subscriber subscriber) throws EmailInvalidException {
        if (isSubscriberExist(subscriber)) {
            throw new EmailInvalidException("Email already exists");
        }

        if (subscriber.getSkills() != null) {
            List<Long> skillIds = subscriber.getSkills().stream().map(skill -> skill.getId()).toList();
            subscriber.setSkills(this.skillService.handleGetSkillsByIds(skillIds));
        }
        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber handleUpdateSubscriber(Subscriber subscriber) throws IdInvalidException {
        Subscriber dbSubscriber = this.handleGetSubscriberById(subscriber.getId());
        if (dbSubscriber == null) {
            throw new IdInvalidException("Subscriber not found");
        }
        List<Long> skillIds = subscriber.getSkills().stream().map(skill -> skill.getId()).toList();
        dbSubscriber.setSkills(this.skillService.handleGetSkillsByIds(skillIds));
        return this.subscriberRepository.save(dbSubscriber);
    }

    public Subscriber handleGetSubscriberById(Long id) {
        return this.subscriberRepository.findById(id).orElse(null);
    }
}
