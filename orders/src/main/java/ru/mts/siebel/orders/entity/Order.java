package ru.mts.siebel.orders.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {

    @Id
    private String id;
    private LocalDate created;
    @Column(name = "status_cd")
    private String status;
    @Column(name = "product_code")
    private String productCode;
    private Integer quantity;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "customer_phone")
    private String phone;
    private String address;

    public String getId() {
        return id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public String getStatus() {
        return status;
    }

    public String getProductCode() {
        return productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
