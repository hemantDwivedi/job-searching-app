package com.inverse.project.Jobless.services.impl;

import com.inverse.project.Jobless.util.ValueMapper;
import com.inverse.project.Jobless.dto.JobDto;
import com.inverse.project.Jobless.exceptions.ResourceNotFoundException;
import com.inverse.project.Jobless.models.Job;
import com.inverse.project.Jobless.models.JobCategory;
import com.inverse.project.Jobless.repositories.JobCategoryRepository;
import com.inverse.project.Jobless.repositories.JobRepository;
import com.inverse.project.Jobless.services.JobService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {


    private final JobRepository jobRepository;


    private final JobCategoryRepository jobCategoryRepository;


    private final ValueMapper valueMapper;

    public JobServiceImpl(JobRepository jobRepository, JobCategoryRepository jobCategoryRepository, ValueMapper valueMapper) {
        this.jobRepository = jobRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.valueMapper = valueMapper;
    }


    @Override
    public JobDto create(JobDto jobDto, Integer jobCategoryId) {
        Job job = this.valueMapper.modelMapper().map(jobDto, Job.class);
        JobCategory jobCategory = this.jobCategoryRepository.findById(jobCategoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Job Category not found ID: " + jobCategoryId)
                );
        job.setJobCategory(jobCategory);
        this.jobRepository.save(job);
        return this.valueMapper.modelMapper().map(job, JobDto.class);
    }

    @Override
    public JobDto update(JobDto jobDto,Integer jobCategoryId, Integer id) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Job not found ID: " + id)
                );
        JobCategory jobCategory = this.jobCategoryRepository.findById(jobCategoryId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Job Category not found ID: " + jobCategoryId)
                        );
        job.setName(jobDto.getName());
        job.setAbout(jobDto.getAbout());
        job.setLocation(jobDto.getLocation());
        job.setStartDate(jobDto.getStartDate());
        job.setApplyBy(jobDto.getApplyBy());
        job.setSkillList(jobDto.getSkillList());
        job.setNumberOfOpening(jobDto.getNumberOfOpening());
        job.setSalary(jobDto.getSalary());
        job.setCompanyName(jobDto.getCompanyName());
        job.setCompanyAbout(jobDto.getCompanyAbout());
        job.setCompanyWebsite(jobDto.getCompanyWebsite());
        job.setJobCategory(jobCategory);
        this.jobRepository.save(job);
        return this.valueMapper.modelMapper().map(job, JobDto.class);
    }

    @Override
    public JobDto getById(Integer id) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Job not found ID: " + id)
                );
        return this.valueMapper.modelMapper().map(job, JobDto.class);
    }

    @Override
    public List<JobDto> getAll() {
        List<Job> jobs = this.jobRepository.findAll();
        return jobs
                .stream()
                .map(
                        job ->  this.valueMapper.modelMapper().map(job, JobDto.class)
                ).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Job not found ID: " + id)
                );
        this.jobRepository.delete(job);
    }
}
