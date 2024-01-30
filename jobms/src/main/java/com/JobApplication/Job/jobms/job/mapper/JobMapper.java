package com.JobApplication.Job.jobms.job.mapper;

import com.JobApplication.Job.jobms.external.Company;
import com.JobApplication.Job.jobms.external.Review;
import com.JobApplication.Job.jobms.job.Job;
import com.JobApplication.Job.jobms.job.dto.JobDTO;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDTO(Job job, Company company, List<Review> reviews){
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setCompany(company);
        jobDTO.setReview(reviews);
        return jobDTO;
    }
}
