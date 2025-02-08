package com.pharmacie.controllers;

import com.pharmacie.models.Stock;
import com.pharmacie.services.StockService;

import java.util.List;

public class StockController {

    private StockService stockService;

    public StockController() {
        this.stockService = new StockService();
    }

    // Méthode pour enregistrer un stock
    public void saveStock(Stock stock) {
        stockService.saveStock(stock);
    }

    // Méthode pour récupérer un stock par son ID
    public Stock getStockById(int id) {
        return stockService.getStockById(id);
    }

    // Méthode pour récupérer tous les stocks
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    // Méthode pour mettre à jour un stock
    public void updateStock(Stock stock) {
        stockService.updateStock(stock);
    }

    // Méthode pour supprimer un stock par son ID
    public void deleteStock(int id) {
        stockService.deleteStock(id);
    }
}
