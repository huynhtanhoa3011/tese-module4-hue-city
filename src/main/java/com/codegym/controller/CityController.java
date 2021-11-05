package com.codegym.controller;

import com.codegym.model.City;
import com.codegym.service.city.ICityService;
import com.codegym.service.country.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/cities")
public class CityController {
    @Autowired
    private ICityService cityService;
    @Autowired
    private ICountryService countryService;

    @GetMapping("")
    public ModelAndView getList() {
        ModelAndView modelAndView = new ModelAndView("cities/list");
        modelAndView.addObject("city", cityService.findAll());
        return modelAndView;
    }


    @GetMapping("/create")
    public ModelAndView showFormCreate() {
        ModelAndView modelAndView = new ModelAndView("cities/create");
        modelAndView.addObject("city", new City());
        modelAndView.addObject("countries", countryService.findAll());
        modelAndView.addObject("success", null);
        return modelAndView;
    }
    @PostMapping("/create")
    public ModelAndView save (@Validated @ModelAttribute("city") City city, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if(bindingResult.hasFieldErrors()) {
            modelAndView.setViewName("cities/create");
            modelAndView.addObject("countries", countryService.findAll());
            modelAndView.addObject("script", true);

        } else {
            if (cityService.existsByName(city.getName())) {
                modelAndView.setViewName("cities/create");
                modelAndView.addObject("countries", countryService.findAll());
                modelAndView.addObject("error", "City already exists");

            } else {
                cityService.save(city);
                redirectAttributes.addFlashAttribute("success", "Successfully added new city");
                modelAndView.setViewName("redirect:/cities");
            }
        }
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showFormEdit(@PathVariable Long id) {
        Optional<City> city = cityService.findById(id);

        if(city.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("cities/edit");
            modelAndView.addObject("city", city.get());
            modelAndView.addObject("countries", countryService.findAll());
            modelAndView.addObject("success", null);
            return modelAndView;
        } else {
            return new ModelAndView("error.404");
        }
    }
    @PostMapping("/edit/{id}")
    public ModelAndView updateCity(@ModelAttribute("city") City city,@PathVariable Long id) {
        cityService.save(city);
        ModelAndView modelAndView = new ModelAndView("/cities/edit");
        modelAndView.addObject("city", city);
        modelAndView.addObject("success", "City updated successfully");
        return modelAndView;
    }


    @GetMapping("/delete/{id}")
    public ModelAndView showFormDelete(@PathVariable Long id) {
        Optional<City> city = cityService.findById(id);
        if(city.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("cities/delete");
            modelAndView.addObject("city", city.get());
            modelAndView.addObject("countries", countryService.findAll());
            modelAndView.addObject("success", null);
            return modelAndView;
        } else {
            return new ModelAndView("error.404");
        }
    }
    @PostMapping("/delete")
    public ModelAndView deleteCity(@ModelAttribute("city") City city) {
        try{
            cityService.remove(city.getId());
            return new ModelAndView("redirect:cities") ;
        }catch (Exception e){
            return new ModelAndView("/error.404");
        }
    }


    @GetMapping("/info/{id}")
    public ModelAndView showFormInfo(@PathVariable Long id) {
        Optional<City> city = cityService.findById(id);

        if(city.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("cities/info");
            modelAndView.addObject("city", city.get());
            modelAndView.addObject("success", null);
            return modelAndView;
        } else {
            return new ModelAndView("error.404");
        }
    }
}
