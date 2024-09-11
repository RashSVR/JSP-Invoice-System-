package com.is.invoicingsystem.dto;

import com.is.invoicingsystem.model.Item;
import lombok.Data;

@Data
public class InvoiceItemObject {
    Item item;
    Long quantity;
}
