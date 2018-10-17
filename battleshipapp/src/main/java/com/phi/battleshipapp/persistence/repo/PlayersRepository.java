package com.phi.battleshipapp.persistence.repo;

import com.phi.battleshipapp.persistence.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface PlayersRepository extends JpaRepository<Player, Long> {

}
