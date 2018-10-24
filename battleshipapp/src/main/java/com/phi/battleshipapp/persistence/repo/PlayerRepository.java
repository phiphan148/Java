package com.phi.battleshipapp.persistence.repo;

import com.phi.battleshipapp.persistence.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByUsername(@Param("username") String username);
}
