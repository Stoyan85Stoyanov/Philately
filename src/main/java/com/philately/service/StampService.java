package com.philately.service;

import com.philately.config.UserSession;
import com.philately.model.dto.AddStampDto;
import com.philately.model.entity.Paper;
import com.philately.model.entity.Stamp;
import com.philately.model.entity.User;
import com.philately.model.entity.enums.PaperName;
import com.philately.repository.PaperRepository;
import com.philately.repository.StampRepository;
import com.philately.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class StampService {

    private final UserSession userSession;
    private final UserRepository userRepository;
    private final PaperRepository paperRepository;
    private final StampRepository stampRepository;

    public StampService(UserSession userSession, UserRepository userRepository, PaperRepository paperRepository, StampRepository stampRepository) {
        this.userSession = userSession;
        this.userRepository = userRepository;
        this.paperRepository = paperRepository;
        this.stampRepository = stampRepository;
    }

    public boolean create(AddStampDto addStampDto) {

        if (!userSession.isLoggedIn()) {
            return false;
        }

        Optional<User> byId = userRepository.findById(userSession.id());

        if (byId.isEmpty()) {
            return false;
        }
        Optional<Paper> byName = Optional.ofNullable(paperRepository.findByPaperName(PaperName.valueOf(addStampDto.getPaper())));

        if (byName.isEmpty()) {
            return false;
        }

        Stamp stamp = new Stamp();
        stamp.setName(addStampDto.getName());
        stamp.setDescription(addStampDto.getDescription());
        stamp.setImageUrl(addStampDto.getImageUrl());
        stamp.setPrice(addStampDto.getPrice());
        stamp.setPaper(byName.get());
        stamp.setOwner(byId.get());

        stampRepository.save(stamp);
        return true;
    }


    public List<Stamp> listStamps(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return stampRepository.findAllByOwner(user);
    }

    public List<Stamp> getAll() {
        return stampRepository.findAll();
    }


    public void toWishlist(UUID stampId, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Optional<Stamp> stamp = stampRepository.findById(stampId);

        user.getWishedStamps().add(stamp.get());
        userRepository.save(user);
    }

    @Transactional
    public void removeFromWishlist(UUID stampId, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        Optional<Stamp> stamp = stampRepository.findById(stampId);

        user.getWishedStamps().remove(stamp.get());
        userRepository.save(user);
    }


    public void buyAllMyWishlist() {
        User buyer = userRepository.findByUsername(userSession.username()).orElse(null);

        if (buyer == null) {
            return;
        }

        List<Stamp> wishedStamps = new ArrayList<>(buyer.getWishedStamps());

        for (Stamp wishedStamp : wishedStamps) {
            buyer.getWishedStamps().remove(wishedStamp);
            buyer.getPurchasedStamps().add(wishedStamp);
        }
        userRepository.save(buyer);
    }
}
