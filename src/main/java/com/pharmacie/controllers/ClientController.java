package com.pharmacie.controllers;


import com.pharmacie.models.Client;
import com.pharmacie.services.ClientService;

import java.util.List;

public class ClientController {

    private ClientService clientService;

    public ClientController() {
        this.clientService = new ClientService();  // Instancier le service
    }


    // Mettre à jour un client
    public void updateClient(int id, String name, String email, String contact) {
        Client client = clientService.getClientById(id);
        if (client != null) {
            client.setName(name);
            client.setEmail(email);
            client.setContact(contact);
            clientService.updateClient(client);
        }
    }

    // Supprimer un client
    public void deleteClient(int id) {
        clientService.deleteClient(id);
    }

    // Afficher tous les clients
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    // Afficher un client par ID
    public Client getClientById(int id) {
        return clientService.getClientById(id);
    }

    public void save(Client client) {
        clientService.saveClient(client);
    }
}
