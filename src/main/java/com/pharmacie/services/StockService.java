package com.pharmacie.services;


import com.pharmacie.dao.StockDAO;
import com.pharmacie.models.Stock;

import java.util.List;

public class StockService {

    private StockDAO stockDAO;

    public StockService() {
        this.stockDAO = new StockDAO();
    }

    // Méthode pour enregistrer un stock
    public void saveStock(Stock stock) {
        stockDAO.save(stock);
    }

    // Méthode pour récupérer un stock par son ID
    public Stock getStockById(int id) {
        return stockDAO.getById(id);
    }

    // Méthode pour récupérer tous les stocks
    public List<Stock> getAllStocks() {
        return stockDAO.getAll();
    }

    // Méthode pour mettre à jour un stock
    public void updateStock(Stock stock) {
        stockDAO.update(stock);
    }

    // Méthode pour supprimer un stock par son ID
    public void deleteStock(int id) {
        stockDAO.delete(id);
    }
}
