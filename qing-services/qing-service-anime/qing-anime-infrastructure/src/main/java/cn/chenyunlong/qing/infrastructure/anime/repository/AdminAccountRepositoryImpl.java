package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.auth.admin.AdminAccount;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.AdminAccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminAccountRepositoryImpl implements AdminAccountRepository {

    private final AdminAccountJpaRepository adminAccountJpaRepository;


    @Override
    public AdminAccount save(AdminAccount entity) {
        return null;
    }

    @Override
    public Optional<AdminAccount> findById(AggregateId id) {
        return Optional.empty();
    }
}
