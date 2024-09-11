package com.is.invoicingsystem.dto;

import com.is.invoicingsystem.model.Invoice;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceItemsResponse {
    Invoice invoice;
    List<InvoiceItemObject> invoiceItems;
}
