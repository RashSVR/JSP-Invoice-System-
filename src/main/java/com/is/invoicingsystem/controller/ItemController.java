package com.is.invoicingsystem.controller;

import com.google.gson.Gson;
import com.is.invoicingsystem.dao.ItemDao;
import com.is.invoicingsystem.model.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "items", value = "/items")
public class ItemController extends HttpServlet {
    private ItemDao itemDao;
    private Gson gson = new Gson();

    public void init() {
        itemDao = new ItemDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
        List<Item> items = new ArrayList<>();
        Long id = Long.parseLong(Objects.nonNull(request.getParameter("id")) ? request.getParameter("id") : "0");
        if (id != 0) {
            Item item = itemDao.getItemById(id);
            if (Objects.nonNull(item)) {
                items.add(item);
            }
        } else {
            items = itemDao.getAllItems();
        }
        System.out.println("items = " + items);
        String itemJson = gson.toJson(items);
        response.setContentType("application/json");
        response.getWriter().print(itemJson);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("items");
            return;
        }

        switch (action) {
            case "insert":
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
        try {
            Item item = getItemFromRequest(request);
            itemDao.saveItem(item);

            // Return the saved item as JSON
            String itemJson = gson.toJson(item);
            response.setContentType("application/json");
            response.getWriter().write(itemJson);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to save item.");
        }
    }

    private void editItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Item item = getItemFromRequest(request);
            itemDao.updateItem(item);

            // Return the updated item as JSON
            String itemJson = gson.toJson(item);
            response.setContentType("application/json");
            response.getWriter().write(itemJson);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update item.");
        }
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            itemDao.deleteItem(id);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete item.");
        }
    }

    private Item getItemFromRequest(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.parseLong(idStr) : null;
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        Long quantity = Long.parseLong(request.getParameter("quantity"));
        BigDecimal price = new BigDecimal(request.getParameter("price"));

        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setQuantity(quantity);
        item.setPrice(price);

        return item;
    }
}
