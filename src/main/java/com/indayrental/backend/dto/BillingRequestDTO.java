package com.indayrental.backend.dto;

import java.time.LocalDate;

public class BillingRequestDTO {
    private Long tenantId;
    private Long roomId;
    private Double rentAmount;
    private String electricityReadingImg;
    private Double previousElectricityReading;
    private Double currentElectricityReading;
    private Double electricityRatePerKwh;
    private Double electricityBill;
    private Double waterBill;
    private Double totalAmount;
    private LocalDate billingDate;
    private LocalDate dueDate;
    private String status;
    private LocalDate datePaid;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Double getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(Double rentAmount) {
        this.rentAmount = rentAmount;
    }

    public String getElectricityReadingImg() {
        return electricityReadingImg;
    }

    public void setElectricityReadingImg(String electricityReadingImg) {
        this.electricityReadingImg = electricityReadingImg;
    }

    public Double getPreviousElectricityReading() {
        return previousElectricityReading;
    }

    public void setPreviousElectricityReading(Double previousElectricityReading) {
        this.previousElectricityReading = previousElectricityReading;
    }

    public Double getCurrentElectricityReading() {
        return currentElectricityReading;
    }

    public void setCurrentElectricityReading(Double currentElectricityReading) {
        this.currentElectricityReading = currentElectricityReading;
    }

    public Double getElectricityRatePerKwh() {
        return electricityRatePerKwh;
    }

    public void setElectricityRatePerKwh(Double electricityRatePerKwh) {
        this.electricityRatePerKwh = electricityRatePerKwh;
    }

    public Double getElectricityBill() {
        return electricityBill;
    }

    public void setElectricityBill(Double electricityBill) {
        this.electricityBill = electricityBill;
    }

    public Double getWaterBill() {
        return waterBill;
    }

    public void setWaterBill(Double waterBill) {
        this.waterBill = waterBill;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(LocalDate datePaid) {
        this.datePaid = datePaid;
    }
}