package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SellerSumDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query(	"SELECT new com.devsuperior.dsmeta.dto.SaleMinDTO(obj.id, obj.date, obj.amount, obj.seller.name) " + 
			"FROM Sale obj " +
			"WHERE obj.date BETWEEN :startDate AND :endDate " +
			"AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))")
	Page<SaleMinDTO> salesReport(LocalDate startDate, LocalDate endDate, String name, Pageable pageable);
	
	@Query(	"SELECT new com.devsuperior.dsmeta.dto.SellerSumDTO(obj.seller.name, SUM(obj.amount)) " 
			+ "FROM Sale obj " 
			+ "WHERE obj.date BETWEEN :startDate AND :endDate " 
			+ "GROUP BY obj.seller.name")
	List<SellerSumDTO> salesSummary(LocalDate startDate, LocalDate endDate);
}
