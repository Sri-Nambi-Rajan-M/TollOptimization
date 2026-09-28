package com.urbantraffic.platform.config;

import com.urbantraffic.platform.crud.Commuter;
import com.urbantraffic.platform.crud.TollTransaction;
import com.urbantraffic.platform.crud.VehicleType;
import com.urbantraffic.platform.entity.Complaint;
import com.urbantraffic.platform.repository.ComplaintRepository;
import com.urbantraffic.platform.repository.crud.CrudCommuterRepository;
import com.urbantraffic.platform.repository.crud.CrudTollTransactionRepository;
import com.urbantraffic.platform.repository.crud.CrudVehicleTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CrudMockDataSeeder implements CommandLineRunner {
    private final CrudVehicleTypeRepository vehicleTypeRepository;
    private final CrudCommuterRepository commuterRepository;
    private final CrudTollTransactionRepository tollTransactionRepository;
    private final ComplaintRepository complaintRepository;

    public CrudMockDataSeeder(
            CrudVehicleTypeRepository vehicleTypeRepository,
            CrudCommuterRepository commuterRepository,
            CrudTollTransactionRepository tollTransactionRepository,
            ComplaintRepository complaintRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.commuterRepository = commuterRepository;
        this.tollTransactionRepository = tollTransactionRepository;
        this.complaintRepository = complaintRepository;
    }

    @Override
    public void run(String... args) {
        if (vehicleTypeRepository.count() == 0) {
            vehicleTypeRepository.saveAll(List.of(
                    vehicleType("Two Wheeler", "Motorcycles and scooters"),
                    vehicleType("Car", "Cars, jeeps and vans for private use"),
                    vehicleType("Light Commercial Vehicle", "Mini trucks and small goods carriers"),
                    vehicleType("Bus", "City and intercity buses"),
                    vehicleType("Heavy Truck", "Multi-axle trucks and trailers")
            ));
        }

        if (commuterRepository.count() == 0) {
            commuterRepository.saveAll(List.of(
                    commuter("Arun Kumar", "arun.kumar@example.com", "9840012345"),
                    commuter("Priya Sharma", "priya.sharma@example.com", "9841123456"),
                    commuter("Karthik Raja", "karthik.raja@example.com", "9942234567"),
                    commuter("Meena Lakshmi", "meena.lakshmi@example.com", "9443345678"),
                    commuter("Suresh Babu", "suresh.babu@example.com", "9600456789")
            ));
        }

        if (tollTransactionRepository.count() == 0) {
            tollTransactionRepository.saveAll(List.of(
                    transaction("arun.kumar@example.com", "Car", "85.00", "PAID"),
                    transaction("priya.sharma@example.com", "Two Wheeler", "25.00", "PAID"),
                    transaction("karthik.raja@example.com", "Bus", "170.00", "PENDING"),
                    transaction("meena.lakshmi@example.com", "Car", "85.00", "PAID"),
                    transaction("suresh.babu@example.com", "Heavy Truck", "290.00", "FAILED"),
                    transaction("arun.kumar@example.com", "Car", "85.00", "PENDING")
            ));
        }

        if (complaintRepository.count() == 0) {
            complaintRepository.saveAll(List.of(
                    complaint("Double charged at Guindy Gate",
                            "I was charged twice for a single trip on Monday morning. Please refund.",
                            "arun.kumar@example.com", "OPEN"),
                    complaint("Toll lane barrier did not open",
                            "The barrier stayed closed after payment was confirmed, causing a queue.",
                            "priya.sharma@example.com", "IN_PROGRESS"),
                    complaint("Wrong vehicle class billed",
                            "My car was billed at the bus rate at Central Toll Plaza.",
                            "karthik.raja@example.com", "RESOLVED")
            ));
        }
    }

    private static VehicleType vehicleType(String name, String description) {
        VehicleType vehicleType = new VehicleType();
        vehicleType.setName(name);
        vehicleType.setDescription(description);
        return vehicleType;
    }

    private static Commuter commuter(String name, String email, String phone) {
        Commuter commuter = new Commuter();
        commuter.setName(name);
        commuter.setEmail(email);
        commuter.setPhone(phone);
        return commuter;
    }

    private static TollTransaction transaction(String commuterEmail, String vehicleType,
                                               String amount, String paymentStatus) {
        TollTransaction transaction = new TollTransaction();
        transaction.setCommuterEmail(commuterEmail);
        transaction.setVehicleType(vehicleType);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setPaymentStatus(paymentStatus);
        return transaction;
    }

    private static Complaint complaint(String subject, String description,
                                       String customerEmail, String status) {
        Complaint complaint = new Complaint();
        complaint.setSubject(subject);
        complaint.setDescription(description);
        complaint.setCustomerEmail(customerEmail);
        complaint.setStatus(status);
        return complaint;
    }
}