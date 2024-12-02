package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.List;
@Service //this class contains business logic and will be managed as a service bean
public class SalesDataService {

    @Autowired //inject an instance of SalesDataRepository
    private SalesDataRepository repository;

    public List<SalesData> getAllSales() {
        return repository.findAll();
    }

    public List<SalesData> getFilteredSales(String region, Double minLeadScore, LocalDateTime startDate, LocalDateTime endDate) {
        System.out.println("Region: " + region);
        System.out.println("Min Lead Score: " + minLeadScore);
        System.out.println("Start Interaction Date: " + startDate);
        System.out.println("End Interaction Date: " + endDate);

        return repository.findByFilters(region, minLeadScore, startDate, endDate);
    }

    public List<Object[]> getTotalRevenuePerRegion() {
        return repository.findTotalRevenuePerRegion();
    }



}