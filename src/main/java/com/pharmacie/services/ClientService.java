package com.pharmacie.services;

import com.pharmacie.dao.ClientDAO;
import com.pharmacie.models.Client;
import java.util.List;

public class ClientService {

    private ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();  // Instancier le DAO
    }

    // Enregistrer un client
    public void saveClient(Client client) {
        clientDAO.save(client);
    }

    // Récupérer un client par ID
    public Client getClientById(int id) {
        return clientDAO.getById(id);
    }

    // Récupérer tous les clients
    public List<Client> getAllClients() {
        return clientDAO.getAll();
    }

    // Mettre à jour un client
    public void updateClient(Client client) {
        clientDAO.update(client);
    }

    // Supprimer un client par ID
    public void deleteClient(int id) {
        clientDAO.delete(id);
    }
}
