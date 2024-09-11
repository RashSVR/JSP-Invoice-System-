package com.is.invoicingsystem.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.is.invoicingsystem.dao.InvoiceDao;
import com.is.invoicingsystem.dao.InvoiceItemDao;
import com.is.invoicingsystem.dao.ItemDao;

import com.is.invoicingsystem.dto.InvoiceItemObject;
import com.is.invoicingsystem.dto.InvoiceItemsResponse;
import com.is.invoicingsystem.model.Invoice;
import com.is.invoicingsystem.model.InvoiceItem;
import com.is.invoicingsystem.model.Item;
import com.is.invoicingsystem.service.InvoiceService;
import com.is.invoicingsystem.service.impl.InvoiceServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/invoices")
public class InvoiceController extends HttpServlet {
    InvoiceService invoiceService = new InvoiceServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (Objects.nonNull(action)) {
            switch (action) {
                case "add":
                    invoiceService.addInvoice(request, response);
                    break;
                case "edit":
                    invoiceService.editInvoice(request, response);
                    break;
                case "delete":
                    invoiceService.deleteInvoice(request, response);
                    break;
                case "getItems":
                    invoiceService.getItems(request, response);
                    break;
            }
        } else {
            invoiceService.getInvoices(request, response);
        }
    }


}