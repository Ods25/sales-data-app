package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository; //built-in interface that includes CRUD operations.
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesDataRepository extends JpaRepository<SalesData, Long> { // Makes this repository handle SalesData entities with a primary key of type Long
    // Custom Query to filter by region, date, and lead score

    /* practicing SQL again with JPA bear with me

    SELECT s
    FROM SalesData s //map all from SalesData to var s
    WHERE
        (:region IS NULL OR s.region = :region) //:region is null or region column matches :region
        AND (:minLeadScore IS NULL OR s.leadScore >= :minLeadScore) //minLeadScore is null or leadscore rows grtr than :minleadscore
        AND (:startDate IS NULL OR s.interactionDate >= :startDate)
        AND (:endDate IS NULL OR s.interactionDate <= :endDate)

     */

    @Query("SELECT s FROM SalesData s WHERE " +
            "(:region IS NULL OR s.region = :region) " +
            "AND (:minLeadScore IS NULL OR s.leadScore >= :minLeadScore) " +
            "AND s.interactionDate >= :startDate " +
            "AND s.interactionDate <= :endDate")
    List<SalesData> findByFilters(
            @Param("region") String region,
            @Param("minLeadScore") Double minLeadScore,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    // Aggregation: Total revenue per region
    @Query("SELECT s.region, SUM(s.revenue) FROM SalesData s GROUP BY s.region")
    List<Object[]> findTotalRevenuePerRegion(); //when finddTotalRevenuePerRegion() is run, the previous query will run.



}
