package com.phi.battleshipapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {

}
