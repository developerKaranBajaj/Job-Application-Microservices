package com.JobApplication.Job.jobms.job.impl;

import com.JobApplication.Job.jobms.external.Company;
import com.JobApplication.Job.jobms.external.Review;
import com.JobApplication.Job.jobms.job.Job;
import com.JobApplication.Job.jobms.job.JobRespository;
import com.JobApplication.Job.jobms.job.JobService;
import com.JobApplication.Job.jobms.job.clients.CompanyClient;
import com.JobApplication.Job.jobms.job.clients.ReviewClient;
import com.JobApplication.Job.jobms.job.dto.JobDTO;
import com.JobApplication.Job.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {


    //private List<Job> jobs = new ArrayList<>();
    JobRespository jobRespository;

    @Autowired
    RestTemplate restTemplate;

    int attempt = 0;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    public JobServiceImpl(JobRespository jobRespository,CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRespository = jobRespository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    private Long nextId = 1L;
    @Override
    //@CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    //@Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        System.out.println("Attempt: "+ ++attempt);
        List<Job> jobs = jobRespository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();

        for(Job job:jobs){
            jobDTOS.add(convertToDto(job));
        }


        return jobDTOS;
    }

    public List<String> companyBreakerFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }

    private JobDTO convertToDto(Job job){
        //RestTemplate restTemplate = new RestTemplate();
        //JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
        //jobWithCompanyDTO.setJob(job);

        /*Company company = restTemplate.getForObject(
                "http://COMPANY-SERVICE:8081/companies/"+ job.getCompanyId(), Company.class);*/

        /*ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
                "http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {
                });
        List<Review> reviews = reviewResponse.getBody();*/
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job, company, reviews);



        return jobDTO;


    }
    @Override
    public void createJob(Job job) {
        job.setId(nextId++);
        jobRespository.save(job);

    }


    @Override
    public JobDTO getJobById(Long id) {
        Job job =  jobRespository.findById(id).orElse(null);

        return convertToDto(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
//        Job job = getJobById(id);
//        if(job!=null) {
//            jobs.remove(job);
//            return true;
//        }
        try {
            jobRespository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateJobById(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRespository.findById(id);
            if (jobOptional.isPresent()) {
                Job job = jobOptional.get();
                job.setTitle(updatedJob.getTitle());
                job.setDescription(updatedJob.getDescription());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setLocation(updatedJob.getLocation());
                jobRespository.save(job);
                return true;
            }
        return false;
    }


}
