package com.example.demo.controller;

import com.google.gson.Gson;
import com.example.demo.entity.Client;
import com.example.demo.exception.UserNotFound;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ClientController {

    @Autowired
    private ClientRepository clientRepo;

    private final Gson gson = new Gson();

    @GetMapping("/clients")
    public Iterable<Client> getAll() {
        return clientRepo.findAll();
    }

    @GetMapping("/client/{id}")
    public EntityModel<Client> one(@PathVariable Integer id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new UserNotFound(id));

        return EntityModel.of(client,
                linkTo(methodOn(ClientController.class).one(id)).withSelfRel(),
                linkTo(methodOn(ClientController.class).getAll()).withRel("clients"));
    }

    @GetMapping("validateClient")
    @ResponseBody
    public Client validateClient(@RequestParam String login, @RequestParam String psw) {
        return clientRepo.getClientByLoginAndPassword(login, psw);
    }

    @PutMapping("/updateClient/{id}")
    public @ResponseBody String updateClient(@RequestBody String clientInfoToUpdate,
                                             @PathVariable int id) {

        Properties properties = gson.fromJson(clientInfoToUpdate, Properties.class);

        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new UserNotFound(id));

        client.setName(properties.getProperty("name", client.getName()));
        client.setSurname(properties.getProperty("surname", client.getSurname()));
        client.setCardNumber(properties.getProperty("card", client.getCardNumber()));

        clientRepo.save(client);
        return "Success";
    }
}
