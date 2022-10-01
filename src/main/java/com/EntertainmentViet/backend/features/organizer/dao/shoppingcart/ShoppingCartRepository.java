package com.EntertainmentViet.backend.features.organizer.dao.shoppingcart;

import com.EntertainmentViet.backend.domain.entities.organizer.OrganizerShoppingCart;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ListCartItemParamDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShoppingCartRepository extends IdentifiableRepository<OrganizerShoppingCart> {

  List<OrganizerShoppingCart> findByOrganizerUid(UUID uid, ListCartItemParamDto paramDto, Pageable pageable);
}
