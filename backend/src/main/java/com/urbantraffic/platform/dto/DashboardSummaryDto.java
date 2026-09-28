package com.urbantraffic.platform.dto;

public class DashboardSummaryDto {
    private long totalRoads;
    private long congestedRoads;
    private long severeRoads;
    private long activeIncidents;
    private long totalTollPlazas;
    private double totalTollRevenue;
    private long transactionCount;
    private double congestionRate;
    private long pendingTransactions;
    private long missedTransactions;
    private long complaintCount;

    public long getTotalRoads() {
        return totalRoads;
    }

    public void setTotalRoads(long totalRoads) {
        this.totalRoads = totalRoads;
    }

    public long getCongestedRoads() {
        return congestedRoads;
    }

    public void setCongestedRoads(long congestedRoads) {
        this.congestedRoads = congestedRoads;
    }

    public long getSevereRoads() {
        return severeRoads;
    }

    public void setSevereRoads(long severeRoads) {
        this.severeRoads = severeRoads;
    }

    public long getActiveIncidents() {
        return activeIncidents;
    }

    public void setActiveIncidents(long activeIncidents) {
        this.activeIncidents = activeIncidents;
    }

    public long getTotalTollPlazas() {
        return totalTollPlazas;
    }

    public void setTotalTollPlazas(long totalTollPlazas) {
        this.totalTollPlazas = totalTollPlazas;
    }

    public double getTotalTollRevenue() {
        return totalTollRevenue;
    }

    public void setTotalTollRevenue(double totalTollRevenue) {
        this.totalTollRevenue = totalTollRevenue;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public double getCongestionRate() {
        return congestionRate;
    }

    public void setCongestionRate(double congestionRate) {
        this.congestionRate = congestionRate;
    }

    public long getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(long pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public long getMissedTransactions() {
        return missedTransactions;
    }

    public void setMissedTransactions(long missedTransactions) {
        this.missedTransactions = missedTransactions;
    }

    public long getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(long complaintCount) {
        this.complaintCount = complaintCount;
    }
}
