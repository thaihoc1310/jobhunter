package vn.thaihoc.jobhunter.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.thaihoc.jobhunter.domain.Job;
import vn.thaihoc.jobhunter.domain.Skill;
import vn.thaihoc.jobhunter.domain.Subscriber;
import vn.thaihoc.jobhunter.repository.JobRepository;
import vn.thaihoc.jobhunter.repository.SubscriberRepository;
import vn.thaihoc.jobhunter.util.error.EmailInvalidException;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillService skillService;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillService skillService,
            JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillService = skillService;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
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

    public Subscriber handleGetSubscriberByEmail(String email) throws EmailInvalidException {
        Subscriber sub = this.subscriberRepository.findByEmail(email).orElse(null);
        if (sub == null) {
            throw new EmailInvalidException("Email not found");
        }
        return sub;
    }

    @Async
    @Transactional
    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        // List<ResEmailJob> arr = listJobs.stream().map(
                        // job -> this.convertJobToSendEmail(job)).toList();

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                listJobs);
                    }
                }
            }
        }
    }

    // private ResEmailJob convertJobToSendEmail(Job job) {
    // ResEmailJob res = new ResEmailJob();
    // res.setName(job.getName());
    // res.setSalary(job.getSalary());
    // res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
    // res.setSkills(job.getSkills().stream().map(
    // skill -> new ResEmailJob.SkillEmail(skill.getName())).toList());
    // return res;
    // }
}
