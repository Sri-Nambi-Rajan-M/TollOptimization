package com.urbantraffic.platform.service;

import com.urbantraffic.platform.dto.DashboardSummaryDto;
import com.urbantraffic.platform.entity.CongestionLevel;
import com.urbantraffic.platform.entity.IncidentStatus;
import com.urbantraffic.platform.entity.PaymentStatus;
import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.repository.ComplaintRepository;
import com.urbantraffic.platform.repository.IncidentRepository;
import com.urbantraffic.platform.repository.RoadRepository;
import com.urbantraffic.platform.repository.TollPlazaRepository;
import com.urbantraffic.platform.repository.TollTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    private final RoadRepository roadRepository;
    private final IncidentRepository incidentRepository;
    private final TollPlazaRepository tollPlazaRepository;
    private final TollTransactionRepository tollTransactionRepository;
    private final ComplaintRepository complaintRepository;

    public DashboardService(RoadRepository roadRepository, IncidentRepository incidentRepository, TollPlazaRepository tollPlazaRepository,
                            TollTransactionRepository tollTransactionRepository, ComplaintRepository complaintRepository) {
        this.roadRepository = roadRepository;
        this.incidentRepository = incidentRepository;
        this.tollPlazaRepository = tollPlazaRepository;
        this.tollTransactionRepository = tollTransactionRepository;
        this.complaintRepository = complaintRepository;
    }

    public DashboardSummaryDto getSummary() {
        List<Road> roads = roadRepository.findAll();
        DashboardSummaryDto dto = new DashboardSummaryDto();
        dto.setTotalRoads(roads.size());
        long congestedRoads = roads.stream().filter(r -> r.getCurrentStatus() == CongestionLevel.MODERATE || r.getCurrentStatus() == CongestionLevel.HIGH || r.getCurrentStatus() == CongestionLevel.SEVERE).count();
        dto.setCongestedRoads(congestedRoads);
        dto.setSevereRoads(roads.stream().filter(r -> r.getCurrentStatus() == CongestionLevel.SEVERE).count());
        dto.setActiveIncidents(incidentRepository.findByStatus(IncidentStatus.ACTIVE).size());
        dto.setTotalTollPlazas(tollPlazaRepository.count());
        dto.setTotalTollRevenue(tollTransactionRepository.sumAmountByStatus(PaymentStatus.PAID));
        dto.setTransactionCount(tollTransactionRepository.count());
        dto.setCongestionRate(roads.isEmpty() ? 0 : Math.round(congestedRoads * 1000.0 / roads.size()) / 10.0);
        dto.setPendingTransactions(tollTransactionRepository.countByStatus(PaymentStatus.PENDING));
        dto.setMissedTransactions(tollTransactionRepository.countByStatus(PaymentStatus.MISSED));
        dto.setComplaintCount(complaintRepository.count());
        return dto;
    }
}
