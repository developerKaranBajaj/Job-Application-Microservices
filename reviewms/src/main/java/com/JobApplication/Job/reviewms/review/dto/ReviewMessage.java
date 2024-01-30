package com.JobApplication.Job.reviewms.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewMessage {
    private Long id;
    private String description;
    private String title;
    private double rating;
    private Long companyId;
}
