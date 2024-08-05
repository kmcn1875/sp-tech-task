package com.mcn.sp.tech.task.repo;

import com.mcn.sp.tech.task.entity.WalletPassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletPassRepo extends JpaRepository<WalletPassEntity, Long> {
}
