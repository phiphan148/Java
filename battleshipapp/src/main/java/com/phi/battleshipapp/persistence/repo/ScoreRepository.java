package com.phi.battleshipapp.persistence.repo;

import com.phi.battleshipapp.persistence.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ScoreRepository extends JpaRepository<Score, Long> {

}
