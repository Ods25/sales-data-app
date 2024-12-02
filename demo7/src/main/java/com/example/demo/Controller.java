package com.example.demo; //gotta put this class in the same package as the main class so Spring finds

//import needed web annotations

//inject services (or other dependencies) into this class, SalesDataService as seen below
import org.springframework.beans.factory.annotation.Autowired;
//for methods mapped to a GET http req
import org.springframework.web.bind.annotation.GetMapping;
//can extract sql query parameters with reqparam from the url
import org.springframework.web.bind.annotation.RequestParam;
//combo of @controller & @responsebody, this is a controller whose methods return http responses
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List; //gets us some func's for list data manip
import java.util.stream.Collectors; //and ssome collection object data manip

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from Vite's dev server
public class Controller { //This class defines endpoints (/sales, /sales/filter, and /sales/summary) we can call to get sales data.

    @Autowired
    private SalesDataService service;

    // Fetch all sales data
    @GetMapping("/sales")
    public List<SalesData> getAllSales() { //this method calls service.getAllSales() to fetch all sales data from the database.
        return service.getAllSales(); //Return a List of SalesData objects (all records from the database) in JSON
    }

    // Filter sales by region, date, or lead score
    @GetMapping("/sales/filter")
    public List<SalesData> getFilteredSales(
            @RequestParam(required = false) String region, //Extracts query parameters from the URL
            @RequestParam(required = false) Double minLeadScore,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {

        // Provide default values if null, postgreSQL cannot handle null values
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : LocalDateTime.of(1900, 1, 1, 0, 0);
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : LocalDateTime.now();

        System.out.println("Region: " + region);
        System.out.println("Min Lead Score: " + minLeadScore);
        System.out.println("Start Date: " + start);
        System.out.println("End Date: " + end);
        return service.getFilteredSales(region, minLeadScore, start, end); //return a list of sales records matching the criteria in JSON
    }

    // Aggregate data: Total revenue per region
    @GetMapping("/sales/summary")
    public List<String> getTotalRevenuePerRegion() { //Returns the total revenue for each region as a summary.
        return service.getTotalRevenuePerRegion().stream()
                .map(result -> "Region: " + result[0] + ", Total Revenue: " + result[1])
                .collect(Collectors.toList());
    }



}
