package com.is.invoicingsystem.service.impl;

import com.google.gson.Gson;
import com.is.invoicingsystem.dao.ItemDao;
import com.is.invoicingsystem.model.Item;
import com.is.invoicingsystem.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemServiceImpl implements ItemService {
    private ItemDao itemDao = new ItemDao();
    private Gson gson = new Gson();

    public void getItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Item> items = new ArrayList<>();
        String idParam = request.getParameter("id");
        Long id = 0L;

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                id = Long.parseLong(idParam);
            }

            if (id != 0) {
                Item item = itemDao.getItemById(id);
                if (item != null) {
                    items.add(item);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item with ID " + id + " not found.");
                    return;
                }
            } else {
                items = itemDao.getAllItems();
            }

            String itemJson = gson.toJson(items);
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

    public void addItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Item item = getItemFromRequest(request);
            itemDao.saveItem(item);
            String itemJson = gson.toJson(item);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(itemJson);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to save item: " + e.getMessage());
        }
    }

    public void editItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Item item = getItemFromRequest(request);
            if (item.getId() == null || itemDao.getItemById(item.getId()) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item with ID " + item.getId() + " not found.");
                return;
            }
            itemDao.updateItem(item);
            String itemJson = gson.toJson(item);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(itemJson);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update item: " + e.getMessage());
        }
    }

    public void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Item item = itemDao.getItemById(id);
            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item with ID " + id + " not found.");
                return;
            }
            itemDao.deleteItem(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' parameter: must be a valid number.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete item: " + e.getMessage());
        }
    }

    private Item getItemFromRequest(HttpServletRequest request) throws Exception {
        try {
            String idStr = request.getParameter("id");
            Long id = (idStr != null && !idStr.isEmpty()) ? Long.parseLong(idStr) : null;
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String quantityStr = request.getParameter("quantity");
            String priceStr = request.getParameter("price");

            System.out.println("name = "+name+"name = "+description+"name = "+quantityStr+"name = "+priceStr);

            if (quantityStr == null || priceStr == null) {
                throw new Exception("Quantity and price must not be null.");
            }
            Long quantity = Long.parseLong(quantityStr);
            BigDecimal price = new BigDecimal(priceStr);

            Item item = new Item();
            item.setId(id);
            item.setName(name);
            item.setDescription(description);
            item.setQuantity(quantity);
            item.setPrice(price);

            return item;
        } catch (NumberFormatException e) {
            throw new Exception("Invalid format for number fields: quantity or price.");
        } catch (Exception e) {
            throw new Exception("Invalid input: " + e.getMessage());
        }
    }


}
