package com.pharmacie.FxControllers.pops;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.pharmacie.FxControllers.cards.ClientCard;
import com.pharmacie.controllers.ClientController;
import com.pharmacie.models.Client;
import com.pharmacie.utilities.Dialogs;

import org.hibernate.exception.ConstraintViolationException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientChoose implements Initializable{

    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    ClientController clientController = new ClientController();

    Client client = null;

    @FXML
    private VBox clientBox;

    @FXML
    private TextField searcher;

    @FXML
    private TextField contactTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField nameTF;


    @FXML
    void addClient(ActionEvent event) {
        String name = nameTF.getText().trim();
        String email = emailTF.getText().trim();
        String contact = contactTF.getText().trim();

        // Vérification que le nom n'est pas vide
        if (name.isEmpty()) {
            Dialogs.showSimpleMessage("Le nom est obligatoire !");
            return;
        }

        // Validation de l'email s'il est spécifié
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            Dialogs.showSimpleMessage("L'email fourni n'est pas valide !");
            return;
        }

        // Validation du contact s'il est spécifié (exemple de validation simple pour un numéro de téléphone)
        if (!contact.isEmpty() && !contact.matches("\\d{10}")) { // Par exemple, 10 chiffres
            Dialogs.showSimpleMessage("Le contact invalide");
            return;
        }

        // Création d'un nouveau client
        Client newClient = new Client();
        newClient.setName(name);
        newClient.setEmail(email.isEmpty() ? null : email); // Assigner null si vide
        newClient.setContact(contact.isEmpty() ? null : contact); // Assigner null si vide
        
        try {
            clientController.save(newClient);  
        } catch (ConstraintViolationException e) {
            if(e.getConstraintName().equals("uk_ow9p33mvkb9m57k6aol2aep0j"))
                Dialogs.showSimpleMessage("un client possede deja ce contact");
            if(e.getConstraintName().equals("uk_srv16ica2c1csub334bxjjb59"))
                Dialogs.showSimpleMessage("un client possede deja ce email");
                // org.hibernate.PropertyValueException:
            return;    
        }

        
        try {
            addClientCard(newClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void onClose(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    void onSearch(ActionEvent event) {

    }

    @FXML
    void onValidate(ActionEvent event) {
        closeWindow(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClients() throws IOException {
        for(Client client : clientController.getAllClients()) {
            addClientCard(client);
        }
    }

    private void addClientCard(Client client) throws IOException {
        try{
            // Charger une carte category
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/clientCard.fxml"));
            Parent clientCard = loader.load(); // Charger le fichier FXML

            ClientCard clientCardController = loader.getController(); // Récupérer le contrôleur associé
            
            clientCard.setUserData(clientCardController);

            clientCardController.setData(client);

            clientCard.setOnMouseClicked(event -> styleBinder(clientCard));

            clientBox.getChildren().add(clientCard);
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void styleBinder(Parent card) {
        // Récupérer le contrôleur associé à la carte actuelle
        ClientCard clientCard = (ClientCard) card.getUserData();
        
        // Si multipleSelector n'est pas sélectionné, itérer sur les enfants du supplierGrid
        for (Node node : clientBox.getChildren()) {
            // Vérifier si l'élément a un contrôleur (on suppose que chaque node a un contrôleur associé)
            if (node.getUserData() != null && node != card) {
                // Récupérer le contrôleur de chaque carte
                ClientCard otherCategoryCardController = (ClientCard) node.getUserData();
                // Appeler la méthode removeSelection() pour retirer le style "selected"
                otherCategoryCardController.removeSelection();
            }
        }
        clientCard.toggle();

        if(clientCard.isCardSelected())
            client = clientCard.getClient();
        else
            client = null;
    }

    public Client getClient() {
        return client;
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        stage.close();
    }
}
