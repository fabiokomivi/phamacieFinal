package com.pharmacie.FxControllers.topSections;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.pharmacie.FxControllers.cards.DashboardPurchase;
import com.pharmacie.helper.SalesServices;
import com.pharmacie.models.Purchase;
import com.pharmacie.models.User;
import com.pharmacie.session.SessionUtil;
import com.pharmacie.utilities.Updatable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Dashboard implements Initializable, Updatable {

    List<String> days = new ArrayList<>(Arrays.asList("lun", "mar", "mer", "jeu", "ven", "sam", "dim"));

    SalesServices salesServices = new SalesServices();

    User user = null;


    @FXML
    private Label emailTop;

    @FXML
    private Label welcomeName;

    @FXML
    private Circle imageCircle;

    @FXML
    private Label nbRecentValidate;

    @FXML
    private Label nbRecentWaiting;

    @FXML
    private Label nbValidatePurchase;

    @FXML
    private Label nbWaitingPurshase;

    @FXML
    private Label totalValidatePurchase;

    @FXML
    private Label totalWaitingPurchase;

    @FXML
    private BarChart<String, Number> purchaseTotalChart;

    @FXML
    private AreaChart<String, Number> purchaseNumberChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label nameTop;

    @FXML
    private VBox validatePurchaseBox;

    @FXML
    private VBox waitingPurchaseBox;

    @FXML
    void onNavButtonSelected(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nameTop.setText(user.getName());
        welcomeName.setText(user.getName());
        emailTop.setText(user.getEmail());
    }

    public void loadData() throws IOException {

        user = SessionUtil.getCurrentUser();

        purchaseTotalChart.getData().clear();
        purchaseNumberChart.getData().clear();

        XYChart.Series<String, Number> purchaseTotalSeries = new XYChart.Series<>();
        purchaseTotalSeries.setName("Ventes hebdomadaires");

        XYChart.Series<String, Number> purchaseSizeSeries = new XYChart.Series<>();
        purchaseSizeSeries.setName("Données simulées");

        // Récupérer les ventes par jour de la semaine
        Map<String, List<Purchase>> weekSales = salesServices.getSalesByDayOfWeek(user.getPurchases());

        // Ajouter les données du dictionnaire à la série
        for (Map.Entry<String, List<Purchase>> entry : weekSales.entrySet()) {
            String day = entry.getKey(); // Jour de la semaine (ex : "Lundi")

            purchaseTotalSeries.getData().add(new XYChart.Data<>(day, salesServices.computeAmount(entry.getValue())));
            purchaseSizeSeries.getData().add(new XYChart.Data<>(day, salesServices.countPurchases(entry.getValue())));
        }

        List<Purchase> todaySales = salesServices.getTodaySales(user);

        // Initialiser les compteurs et totaux
        int nbWait = 0;
        double totalWait = 0.0;
        int nbValidate = 0;
        double totalValidate = 0.0;
        
        // Parcourir les achats du jour une seule fois
        for (Purchase purchase : todaySales) {
            if (purchase.getStatus()) {
                nbValidate++;
                totalValidate += purchase.getTotal();
            } else {
                nbWait++;
                totalWait += purchase.getTotal();
            }
        }
        
        // Mettre à jour les valeurs des textes
        nbWaitingPurshase.setText(String.valueOf(nbWait));
        nbRecentWaiting.setText(String.valueOf(nbWait));
        totalWaitingPurchase.setText(String.valueOf(totalWait));
        
        nbValidatePurchase.setText(String.valueOf(nbValidate));
        nbRecentValidate.setText(String.valueOf(nbValidate));
        totalValidatePurchase.setText(String.valueOf(totalValidate));
        


        // Ajouter la série de données au BarChart
        purchaseTotalChart.getData().add(purchaseTotalSeries);
        purchaseNumberChart.getData().add(purchaseSizeSeries);

        purchaseTotalChart.getXAxis().setVisible(false);
        purchaseNumberChart.getXAxis().setVisible(false);

        purchaseTotalChart.setLegendVisible(false);
        purchaseNumberChart.setLegendVisible(false);

        waitingPurchaseBox.getChildren().clear();
        validatePurchaseBox.getChildren().clear();

        for(Purchase purchase : salesServices.getTodaySales(user)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/dashboardPurchase.fxml"));
            Parent root = loader.load();
            DashboardPurchase controller = loader.getController();
            controller.setData(purchase);
            if(purchase.getStatus())
                validatePurchaseBox.getChildren().add(root);
            else
                waitingPurchaseBox.getChildren().add(root);

        }
    }

    @Override
    public void update() {
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("update dashbord");
    }

}


