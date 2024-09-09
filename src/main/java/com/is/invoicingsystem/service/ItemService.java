package com.is.invoicingsystem.service;

import com.is.invoicingsystem.model.Item;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ItemService {

    void getItems(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void addItem(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void editItem(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
