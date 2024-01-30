package com.JobApplication.Job.jobms.job.dto;

import com.JobApplication.Job.jobms.external.Company;
import com.JobApplication.Job.jobms.external.Review;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobDTO {

    private Long id;
    private String title;
    private String description;
    private String minSalary;
    private String maxSalary;
    private String location;
    private Company company;
    private List<Review> review;

}
