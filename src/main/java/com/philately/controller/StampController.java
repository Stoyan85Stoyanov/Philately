package com.philately.controller;

import com.philately.config.UserSession;
import com.philately.model.dto.AddStampDto;
import com.philately.repository.StampRepository;
import com.philately.repository.UserRepository;
import com.philately.service.StampService;
import com.philately.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class StampController {

    private final UserSession userSession;
    private final StampService stampService;
    private final UserService userService;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;

    @Autowired
    public StampController(UserSession userSession, StampService stampService, UserService userService, StampRepository stampRepository, UserRepository userRepository) {
        this.userSession = userSession;
        this.stampService = stampService;
        this.userService = userService;
        this.stampRepository = stampRepository;

        this.userRepository = userRepository;
    }

    @ModelAttribute("stampData")
    public AddStampDto StampData() {
        return new AddStampDto();
    }

    @GetMapping("/add-stamp")
    public String addStamp(Model model) {
        if (!userSession.isLoggedIn()) {
            return "redirect:/";
        }

        if (!model.containsAttribute("stampData")) {
            model.addAttribute("stampData", new AddStampDto());
        }

        return "add-stamp";
    }

    @PostMapping("/add-stamp")
    public String doAddStamp(@Valid @ModelAttribute("stampData") AddStampDto addStampDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes
    ) {

        if (!userSession.isLoggedIn()) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("stampData", addStampDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.stampData", bindingResult);

            return "redirect:/add-stamp";
        }

        boolean success = stampService.create(addStampDto);

        if (!success) {
            redirectAttributes.addFlashAttribute("stampData", addStampDto);

            return "redirect:/add-stamp";
        }
        return "redirect:/home";
    }

    @PostMapping("/stamp/wishlist/add/{id}")
    public String toWishlist(@PathVariable("id") UUID id) {
        if (!userSession.isLoggedIn()) {
            return "redirect:/";
        }

        String username = userSession.username();

        stampService.toWishlist(id, username);

        return "redirect:/home";
    }

    @PostMapping("/stamp/remove/{id}")
    public ModelAndView returnStamp(@PathVariable("id") UUID id) {
        if (!userSession.isLoggedIn()) {
            return new ModelAndView("redirect:/");
        }

        String username = userSession.username();

        stampService.removeFromWishlist(id, username);

        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/stamp/buy")
        public ModelAndView buy() {
        if (!userSession.isLoggedIn()) {
            return new ModelAndView("redirect:/login");
        }

        stampService.buyAllMyWishlist();

        return new ModelAndView("redirect:/home");
    }
}
