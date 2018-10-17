package com.phi.battleshipapp.persistence.repo;

import com.phi.battleshipapp.persistence.model.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {

}
