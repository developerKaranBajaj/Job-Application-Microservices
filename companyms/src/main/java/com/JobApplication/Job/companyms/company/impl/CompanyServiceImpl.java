package com.JobApplication.Job.companyms.company.impl;


import com.JobApplication.Job.companyms.company.Company;
import com.JobApplication.Job.companyms.company.CompanyRespository;
import com.JobApplication.Job.companyms.company.CompanyService;
import com.JobApplication.Job.companyms.company.clients.ReviewClient;
import com.JobApplication.Job.companyms.company.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRespository companyRespository;
    private ReviewClient reviewClient;

    public CompanyServiceImpl(CompanyRespository companyRespository, ReviewClient reviewClient) {
        this.companyRespository = companyRespository;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRespository.findAll();
    }

    @Override
    public boolean updateCompany(Company updatedCompany, Long id) {
        Optional<Company> companyOptional = companyRespository.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            //company.setJobs(updatedCompany.getJobs());
            company.setDescription(updatedCompany.getDescription());
            company.setName(updatedCompany.getName());

            companyRespository.save(company);
            return true;
        }
        return false;
    }

    @Override
    public void createCompany(Company company) {
        companyRespository.save(company);
    }

    @Override
    public boolean deleteCompany(Long id) {
        try {
            companyRespository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRespository.findById(id).orElse(null);
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        Company company = companyRespository.findById(reviewMessage.getCompanyId())
                .orElseThrow( () -> new NotFoundException("Company not Found " + reviewMessage.getCompanyId()));

        double averageRating = reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating(averageRating);
        companyRespository.save(company);

    }
}
