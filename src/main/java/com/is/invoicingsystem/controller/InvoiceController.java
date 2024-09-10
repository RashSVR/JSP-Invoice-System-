package com.is.invoicingsystem.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.is.invoicingsystem.dao.InvoiceDao;
import com.is.invoicingsystem.dao.InvoiceItemDao;
import com.is.invoicingsystem.dao.ItemDao;

import com.is.invoicingsystem.model.Invoice;
import com.is.invoicingsystem.model.InvoiceItem;
import com.is.invoicingsystem.model.Item;
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
    private InvoiceDao invoiceDao;
    private ItemDao itemDao;
    private ObjectMapper objectMapper;
    private InvoiceItemDao invoiceItemDao;
    private Gson gson;
    public void init() {
        invoiceDao = new InvoiceDao();
        itemDao = new ItemDao();
        objectMapper = new ObjectMapper();
        invoiceItemDao = new InvoiceItemDao();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("invoices", invoiceDao.getAllInvoices());
        request.setAttribute("items", itemDao.getAllItems());
        RequestDispatcher dispatcher = request.getRequestDispatcher("pages/invoices.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (Objects.nonNull(action)) {
        switch (action) {
            case "add":
                addInvoice(request, response);
                break;
            case "edit":
                editInvoice(request, response);
                break;
            case "delete":
                deleteInvoice(request, response);
                break;
        }}else {
            getInvoices(request, response);
        }
    }

    private void getInvoices(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Invoice> invoices = new ArrayList<>();
        String idParam = request.getParameter("id");
        Long id = 0L;

        try {
            // Validate id parameter
            if (idParam != null && !idParam.trim().isEmpty()) {
                id = Long.parseLong(idParam);
            }

            // Fetch item by id if present
            if (id != 0) {
                Invoice invoice = invoiceDao.getInvoiceById(id);
                if (invoice != null) {
                    invoices.add(invoice);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item with ID " + id + " not found.");
                    return;
                }
            } else {
                invoices = invoiceDao.getAllInvoices();
            }

            // Return the items as JSON
            String itemJson = gson.toJson(invoices);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(itemJson);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' parameter: must be a valid number.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
            e.printStackTrace();
        }
    }

    private void addInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        double total = Double.parseDouble(request.getParameter("total"));
        List<Map<String, Long>> selectedItems = objectMapper.readValue(
                request.getParameter("selectedItems"),
                new TypeReference<List<Map<String, Long>>>() {});
        Map<Long, Long> resultMap = selectedItems.stream()
                .collect(Collectors.toMap(obj -> obj.get("id"), obj -> obj.get("quantity")));
        Invoice invoice = new Invoice();
        invoice.setDate(new Date()); // Set LocalDate
        invoice.setTotal(BigDecimal.valueOf(total));
        invoiceDao.saveInvoice(invoice);
        String invoiceJson = gson.toJson(invoice);
        resultMap.forEach((k,v)->{
            Item item = itemDao.getItemById(k);
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoice(invoice);
            invoiceItem.setItem(item);
            invoiceItem.setQuantity(v);
            invoiceItemDao.saveInvoiceItame(invoiceItem);
        });
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(invoiceJson);
    }

    private void editInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        double total = Double.parseDouble(request.getParameter("total"));


        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setDate(new Date()); // Set LocalDate
        invoice.setTotal(BigDecimal.valueOf(total));

        invoiceDao.saveInvoice(invoice);
        response.sendRedirect("invoices");
    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        invoiceDao.deleteInvoice(id);
        response.sendRedirect("invoices");
    }
}