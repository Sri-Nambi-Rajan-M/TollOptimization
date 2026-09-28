package com.urbantraffic.platform.controller;

import com.urbantraffic.platform.crud.Commuter;
import com.urbantraffic.platform.crud.TollTransaction;
import com.urbantraffic.platform.crud.VehicleType;
import com.urbantraffic.platform.entity.Complaint;
import com.urbantraffic.platform.repository.ComplaintRepository;
import com.urbantraffic.platform.repository.crud.CrudCommuterRepository;
import com.urbantraffic.platform.repository.crud.CrudTollTransactionRepository;
import com.urbantraffic.platform.repository.crud.CrudVehicleTypeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CrudController {
    private final CrudCommuterRepository commuterRepository;
    private final CrudTollTransactionRepository tollTransactionRepository;
    private final CrudVehicleTypeRepository vehicleTypeRepository;
    private final ComplaintRepository complaintRepository;

    public CrudController(CrudCommuterRepository commuterRepository,
                          CrudTollTransactionRepository tollTransactionRepository,
                          CrudVehicleTypeRepository vehicleTypeRepository,
                          ComplaintRepository complaintRepository) {
        this.commuterRepository = commuterRepository;
        this.tollTransactionRepository = tollTransactionRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.complaintRepository = complaintRepository;
    }

    @GetMapping("/commuters")
    public List<Commuter> getCommuters() {
        return commuterRepository.findAll();
    }

    @PostMapping("/commuters")
    public ResponseEntity<Commuter> createCommuter(@Valid @RequestBody Commuter commuter) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commuterRepository.save(commuter));
    }

    @PutMapping("/commuters/{id}")
    public Commuter updateCommuter(@PathVariable Long id, @Valid @RequestBody Commuter updated) {
        Commuter commuter = commuterRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        commuter.setName(updated.getName());
        commuter.setEmail(updated.getEmail());
        commuter.setPhone(updated.getPhone());
        return commuterRepository.save(commuter);
    }

    @DeleteMapping("/commuters/{id}")
    public ResponseEntity<Void> deleteCommuter(@PathVariable Long id) {
        commuterRepository.delete(commuterRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/toll-transactions")
    public List<TollTransaction> getTollTransactions() {
        return tollTransactionRepository.findAll();
    }

    @PostMapping("/toll-transactions")
    public ResponseEntity<TollTransaction> createTollTransaction(@Valid @RequestBody TollTransaction transaction) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tollTransactionRepository.save(transaction));
    }

    @PutMapping("/toll-transactions/{id}")
    public TollTransaction updateTollTransaction(@PathVariable Long id, @Valid @RequestBody TollTransaction updated) {
        TollTransaction transaction = tollTransactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        transaction.setCommuterEmail(updated.getCommuterEmail());
        transaction.setVehicleType(updated.getVehicleType());
        transaction.setAmount(updated.getAmount());
        transaction.setPaymentStatus(updated.getPaymentStatus());
        return tollTransactionRepository.save(transaction);
    }

    @DeleteMapping("/toll-transactions/{id}")
    public ResponseEntity<Void> deleteTollTransaction(@PathVariable Long id) {
        tollTransactionRepository.delete(tollTransactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle-types")
    public List<VehicleType> getVehicleTypes() {
        return vehicleTypeRepository.findAll();
    }

    @PostMapping("/vehicle-types")
    public ResponseEntity<VehicleType> createVehicleType(@Valid @RequestBody VehicleType vehicleType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleTypeRepository.save(vehicleType));
    }

    @PutMapping("/vehicle-types/{id}")
    public VehicleType updateVehicleType(@PathVariable Long id, @Valid @RequestBody VehicleType updated) {
        VehicleType vehicleType = vehicleTypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        vehicleType.setName(updated.getName());
        vehicleType.setDescription(updated.getDescription());
        return vehicleTypeRepository.save(vehicleType);
    }

    @DeleteMapping("/vehicle-types/{id}")
    public ResponseEntity<Void> deleteVehicleType(@PathVariable Long id) {
        vehicleTypeRepository.delete(vehicleTypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/complaints")
    public List<Complaint> getComplaints() {
        return complaintRepository.findAll();
    }

    @PostMapping("/complaints")
    public ResponseEntity<Complaint> createComplaint(@Valid @RequestBody Complaint complaint) {
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintRepository.save(complaint));
    }

    @PutMapping("/complaints/{id}")
    public Complaint updateComplaint(@PathVariable Long id, @Valid @RequestBody Complaint updated) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        complaint.setSubject(updated.getSubject());
        complaint.setDescription(updated.getDescription());
        complaint.setCustomerEmail(updated.getCustomerEmail());
        complaint.setStatus(updated.getStatus());
        return complaintRepository.save(complaint);
    }

    @DeleteMapping("/complaints/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        complaintRepository.delete(complaintRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return ResponseEntity.noContent().build();
    }
}