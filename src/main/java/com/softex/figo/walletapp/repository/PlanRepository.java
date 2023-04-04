package com.softex.figo.walletapp.repository;

import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByAuthUser(AuthUser user);

}
