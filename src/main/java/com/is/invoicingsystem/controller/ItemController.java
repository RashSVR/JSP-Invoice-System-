package com.is.invoicingsystem.controller;

import com.is.invoicingsystem.dao.ItemDao;
import com.is.invoicingsystem.model.Item;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/items")
public class ItemController extends HttpServlet {
    private ItemDao itemDao;

    public void init() {
        itemDao = new ItemDao();
    }

    // Handle displaying the list of items
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("items", itemDao.getAllItems());
        RequestDispatcher dispatcher = request.getRequestDispatcher("pages/items.jsp");
        dispatcher.forward(request, response);
    }

    // Handle adding, editing, and deleting items
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("items");
            return;
        }

        switch (action) {
            case "add":
                addItem(request, response);
                break;
            case "edit":
                editItem(request, response);
                break;
            case "delete":
                deleteItem(request, response);
                break;
            default:
                response.sendRedirect("items");
                break;
        }
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));

        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(BigDecimal.valueOf(price));

        itemDao.saveItem(item);
        response.sendRedirect("items");
    }

    private void editItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));

        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(BigDecimal.valueOf(price));

        itemDao.saveItem(item);
        response.sendRedirect("items");
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        itemDao.deleteItem(id);
        response.sendRedirect("items");
    }
}