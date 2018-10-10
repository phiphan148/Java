package com.phi.battleshipapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
public abstract class SalvoController {

    @Autowired
    private GameRepository repo;

    @RequestMapping(value = "/api/games", method = RequestMethod.GET)
    public List<Object> getIds(){
        return repo.findAll().stream().map(Game::getId).collect(toList());
    }
}