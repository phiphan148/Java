package com.phi.battleshipapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface PlayersRepository extends JpaRepository<Player, Long> {

}