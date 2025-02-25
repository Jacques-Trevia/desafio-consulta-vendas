package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SellerSumDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}
		
	public Page<SaleMinDTO> report(String startDate, String endDate, String name, Pageable pageable) {

		List<LocalDate> dates = convertData(startDate, endDate);
		LocalDate localdateStart = dates.get(0);
		LocalDate localdateEnd = dates.get(1);

		Page<SaleMinDTO> result = repository.salesReport(localdateStart, localdateEnd, name, pageable);
		return result;
	}


	public List<SellerSumDTO> summary(String startDate, String endDate) {

		List<LocalDate> dates = convertData(startDate, endDate);
		LocalDate localdateStart = dates.get(0);
		LocalDate localdateEnd = dates.get(1);

		List<SellerSumDTO> result = repository.salesSummary(localdateStart, localdateEnd);
		return result;
	}
	
	public List<LocalDate> convertData(String startDate, String endDate) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate localdateStart = (startDate != null) ? LocalDate.parse(startDate, format) : null;
		LocalDate localdateEnd = (endDate != null) ? LocalDate.parse(endDate, format) : null;

		if (localdateStart == null) {
			localdateStart = (localdateEnd != null) ? localdateEnd.minusYears(1L) : LocalDate.now().minusYears(1L);
		}

		if (localdateEnd == null) {
			localdateEnd = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}

		return Arrays.asList(localdateStart, localdateEnd);
	}
}
