package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;



@Entity //Entity marks this class as a JPA entity, meaning it maps to a table in the database
public class SalesData {
    @Id //specifiedd the primary key of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) //config how the primary key is generated (auto by DB)
    private Long id;
    private String customerName;
    private String region;
    private String product;
    private Double revenue;
    private LocalDateTime interactionDate;
    private Double leadScore;

    // just getters and setters :p
    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }
    public String getCustomerName()
    {
        return customerName;
    }
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }
    public String getRegion()
    {
        return region;
    }
    public void setRegion(String region)
    {
        this.region = region;
    }
    public String getProduct()
    {
        return product;
    }
    public void setProduct(String product)
    {
        this.product = product;
    }
    public Double getRevenue()
    {
        return revenue;
    }
    public void setRevenue(Double revenue)
    {
        this.revenue = revenue;
    }
    public LocalDateTime getInteractionDate()
    {
        return interactionDate;
    }
    public void setInteractionDate(LocalDateTime interactionDate)
    {
        this.interactionDate = interactionDate;
    }
    public Double getLeadScore()
    {
        return leadScore;
    }
    public void setLeadScore(Double leadScore)
    {
        this.leadScore = leadScore;
    }


}