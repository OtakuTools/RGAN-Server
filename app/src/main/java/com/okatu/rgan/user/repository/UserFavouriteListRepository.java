package com.okatu.rgan.user.repository;

import com.okatu.rgan.user.feature.model.entity.UserFavouriteList;
import com.okatu.rgan.user.feature.model.entity.UserFavouriteListId;
import com.okatu.rgan.user.model.RganUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFavouriteListRepository extends JpaRepository<UserFavouriteList, UserFavouriteListId> {
    Page<UserFavouriteList> findById_UserAndEnabledTrueOrderByCreatedTimeDesc(RganUser user, Pageable pageable);

    Optional<UserFavouriteList> findByIdAndEnabledTrue(UserFavouriteListId id);
}
