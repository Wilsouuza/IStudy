package com.wilsouuza.Istudy.repository;

import com.wilsouuza.Istudy.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUserId(Long userId);
}
