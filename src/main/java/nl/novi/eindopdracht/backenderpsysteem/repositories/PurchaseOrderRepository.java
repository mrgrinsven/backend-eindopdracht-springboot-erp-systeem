package nl.novi.eindopdracht.backenderpsysteem.repositories;

import nl.novi.eindopdracht.backenderpsysteem.models.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByIsOpenAndVendorNameContainingIgnoreCase(Boolean isOpen, String vendorName);
    List<PurchaseOrder> findByIsOpen(Boolean isOpen);
    List<PurchaseOrder> findByVendorNameContainingIgnoreCase(String vendorName);
}
