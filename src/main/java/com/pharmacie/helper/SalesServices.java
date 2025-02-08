package com.pharmacie.helper;

import java.time.*;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.pharmacie.models.Purchase;
import com.pharmacie.models.User;

public class SalesServices {

    public Map<String, List<Purchase>> getSalesByDayOfWeek(Set<Purchase> list) {
        // Obtenir la date actuelle
        LocalDate today = LocalDate.now();
        // Début de la semaine actuelle (lundi)
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        // Fin de la semaine actuelle (dimanche)
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        // Utiliser LinkedHashMap pour garantir l'ordre des jours de la semaine
        Map<String, List<Purchase>> salesByDay = new LinkedHashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            // Ajouter des jours avec des listes vides initialement
            String dayName = day.getDisplayName(TextStyle.SHORT, Locale.FRENCH);
            salesByDay.put(dayName, new ArrayList<>());
        }

        // Parcourir une seule fois les achats et les organiser par jour
        for (Purchase purchase : list) {
            LocalDate purchaseDate = purchase.getCreatedAt().toLocalDate();
            if (!purchaseDate.isBefore(startOfWeek) && !purchaseDate.isAfter(endOfWeek)) {
                // Récupérer le nom du jour correspondant
                DayOfWeek dayOfWeek = purchaseDate.getDayOfWeek();
                String dayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.FRENCH);
                // Ajouter l'achat à la liste du jour correspondant
                salesByDay.get(dayName).add(purchase);
            }
        }

        return salesByDay;
    }


    public Map<String, List<Purchase>> getSalesByDayOfWeek(List<Purchase> list) {
        // Obtenir la date actuelle
        LocalDate today = LocalDate.now();
        // Début de la semaine actuelle (lundi)
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        // Fin de la semaine actuelle (dimanche)
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        // Utiliser LinkedHashMap pour garantir l'ordre des jours de la semaine
        Map<String, List<Purchase>> salesByDay = new LinkedHashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            // Ajouter des jours avec des listes vides initialement
            String dayName = day.getDisplayName(TextStyle.SHORT, Locale.FRENCH);
            salesByDay.put(dayName, new ArrayList<>());
        }

        // Parcourir une seule fois les achats et les organiser par jour
        for (Purchase purchase : list) {
            LocalDate purchaseDate = purchase.getCreatedAt().toLocalDate();
            if (!purchaseDate.isBefore(startOfWeek) && !purchaseDate.isAfter(endOfWeek)) {
                // Récupérer le nom du jour correspondant
                DayOfWeek dayOfWeek = purchaseDate.getDayOfWeek();
                String dayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.FRENCH);
                // Ajouter l'achat à la liste du jour correspondant
                salesByDay.get(dayName).add(purchase);
            }
        }

        return salesByDay;
    }

    public List<Purchase> getTodaySales(User user) {
        // Obtenir la date actuelle
        LocalDate today = LocalDate.now();
    
        // Parcourir les achats pour trouver ceux réalisés aujourd'hui
        List<Purchase> todaySales = new ArrayList<>();
        for (Purchase purchase : user.getPurchases()) {
            if (purchase.getCreatedAt().toLocalDate().isEqual(today)) {
                todaySales.add(purchase);
            }
        }
    
        return todaySales;
    }

    public double computeAmount(List<Purchase> purchases) {
        double totalAmount = 0.0;
    
        // Parcourir directement la liste des achats pour calculer le total
        for (Purchase purchase : purchases) {
            totalAmount += purchase.getTotal(); // Supposant que Purchase a une méthode getTotal()
        }
    
        return totalAmount;
    }

    public int countPurchases(List<Purchase> purchases) {
        return purchases.size();
    }
}
