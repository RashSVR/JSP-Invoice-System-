package com.is.invoicingsystem.dto;

import lombok.Data;

@Data
public class PrintInvoiceObject {
    String itemName;
    String itemQuantity;
    String itemPricePerUnit;
    String itemTotal;

}
