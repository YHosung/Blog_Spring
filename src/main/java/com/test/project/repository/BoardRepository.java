package com.test.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.test.project.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity,Long> { 
    @Modifying 
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id") 
    void updateHits(@Param("id") Long id);
}
