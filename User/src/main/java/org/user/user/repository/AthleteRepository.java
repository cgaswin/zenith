package org.user.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.user.user.model.Athlete;



@Repository
public interface AthleteRepository extends JpaRepository<Athlete, String> {

}
