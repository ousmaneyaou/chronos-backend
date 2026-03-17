package com.GimPay.Integration_APIs.repository;

import com.GimPay.Integration_APIs.entity.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    List<Watch> findByActiveTrue();
    List<Watch> findByCategoryIdAndActiveTrue(Long categoryId);

    @Query("SELECT w FROM Watch w WHERE w.active = true AND " +
            "(LOWER(w.name) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
            "LOWER(w.brand) LIKE LOWER(CONCAT('%',:q,'%')))")
    List<Watch> search(@Param("q") String query);
}