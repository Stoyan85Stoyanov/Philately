package com.philately.controller;

import com.philately.config.UserSession;
import com.philately.model.entity.Stamp;
import com.philately.model.entity.User;
import com.philately.service.StampService;
import com.philately.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserSession userSession;
    private final StampService stampService;
    private final UserService userService;

    public HomeController(UserSession userSession, StampService stampService, UserService userService) {
        this.userSession = userSession;
        this.stampService = stampService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String nonLoggedIndex() {

        if (userSession.isLoggedIn()) {
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/home")
    public String loggedInIndex(Model model) {

        if (!userSession.isLoggedIn()) {
            return "redirect:/";
        }

        String currentUsername = userSession.username();

        Optional<User> user = userService.getByUsername(currentUsername);
        model.addAttribute("user", user);

        // my stamps
        List<Stamp> stampList = stampService.listStamps(currentUsername);
        model.addAttribute("myStamps", stampList);

        // not mine stamps
        List<Stamp> offeredStamps = stampService.getAll()
                .stream()
                .filter(stamp -> !stamp.getOwner().getUsername().equals(currentUsername))
                .collect(Collectors.toList());

          // Offered Stamps
        Set<Stamp> offers = new HashSet<>(offeredStamps);
        Set<Stamp> purchasedStamps = user.get().getPurchasedStamps();

        Set<Stamp> result = new HashSet<>(offers);
        result.addAll(purchasedStamps);

        Set<Stamp> result2 = new HashSet<>(offers);
        result2.retainAll(purchasedStamps);

        result.removeAll(result2);
        model.addAttribute("offeredStamps", result);

        // My Wishlist
        Set<Stamp> myWishlist = user.get().getWishedStamps();
        model.addAttribute("myWishlist", myWishlist);

         // My Purchases
        model.addAttribute("myPurchases", user.get().getPurchasedStamps());

        return "home";
    }

}
