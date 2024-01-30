package com.JobApplication.Job.companyms.company;

import com.JobApplication.Job.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {

    List<Company> getAllCompanies();
    boolean updateCompany(Company updatedCompany, Long id);
    void createCompany(Company company);

    boolean deleteCompany(Long id);

    Company getCompanyById(Long id);

    void updateCompanyRating(ReviewMessage reviewMessage);
}
