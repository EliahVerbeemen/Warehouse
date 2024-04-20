package kdg.be.Controllers;


import kdg.be.Managers.IIngredientHoeveelheidManager;
import kdg.be.Managers.IIngredientManager;
import kdg.be.Managers.IngredientManager;
import kdg.be.Models.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class IngredientController {

    private IIngredientManager ingredientMgr;


    Logger logger= LoggerFactory.getLogger(IngredientController.class);

    public IngredientController(IngredientManager ingredientManager) {
        ingredientMgr = ingredientManager;


    }


    @GetMapping({"/ingredienten","/ingredienten/{ingredientId}"})
    public String AllIngredients(Model model,@PathVariable(required = false) Integer ingredientId) {

        List<Ingredient> ing = ingredientMgr.getAllIngredients();
        model.addAttribute("Ingredienten", ing);
        Ingredient nieuwIngredient = new Ingredient();
        model.addAttribute("nieuwIngredient", nieuwIngredient);

        model.addAttribute("updateMe",ingredientId);
    return "Ingredienten/IngredientOverview";
    }

  /*  @GetMapping(value = {"/ingredienten/ingredient/{ingredientId}"})
    public String IngredientDetails(@PathVariable Long ingredientId, Model model) {
        Optional<Ingredient> ingredient = ingredientMgr.getIngredientById(ingredientId);
        if (ingredient.isPresent()) {
            model.addAttribute("Ingredient", ingredient.get());
            model.addAttribute("IngredientId", ingredientId);

            return "Ingredienten/IngredientDetails";
        } else {
            logger.warn("error");
            return "errorPagina";
        }
    }*/











    @PostMapping(value = {"/ingredienten/ingredient/create"})
    public ModelAndView NewIngredient(Ingredient ingredient, @PathVariable(required = false) Long ingredientId,Model model) {
        System.out.println(ingredient.getBeschrijving());
        System.out.println(ingredient.getName());

        ingredientMgr.saveIngredient(ingredient);
        List<Ingredient> ingredienten = ingredientMgr.getAllIngredients();
        model.addAttribute("Ingredienten", ingredienten);
        model.addAttribute("nieuwIngredient", new Ingredient());
       /* ModelAndView modelAndView = new ModelAndView("Ingredienten/IngredientOverview");



        modelAndView.addObject("Ingredienten", ingredienten);
        modelAndView.addObject("nieuwIngredient", new Ingredient());*/

        return new ModelAndView("redirect:/ingredienten", (Map<String, ?>) model) /*modelAndView*/;
    }


    @PostMapping("/ingredienten/update/{ingredientId}")
    public ModelAndView SaveIngredient(Ingredient ingredient, @PathVariable(required = false) Long ingredientId, Model model) {
        System.out.println(ingredientId);
        ingredientMgr.updateIngredient(ingredient.getIngredientId(),ingredient);
        List<Ingredient> ing = ingredientMgr.getAllIngredients();
        model.addAttribute("Ingredienten", ing);
        Ingredient nieuwIngredient = new Ingredient();
        model.addAttribute("nieuwIngredient", nieuwIngredient);
        return new ModelAndView("redirect:/ingredienten",(Map<String, ?>)model);
      /*  ModelAndView modelAndView = new ModelAndView();
        Optional<Ingredient> ingredientOptional = ingredientMgr.getIngredientById(ingredientId);
        if (ingredientOptional.isPresent()) {
            modelAndView.setViewName("Ingredienten/IngredientOverview");
            Ingredient oudIngredient = ingredientOptional.get();
            oudIngredient.setName(ingredient.getName());
            oudIngredient.setBeschrijving(ingredient.getBeschrijving());
            ingredientMgr.saveIngredient(oudIngredient);
            //  Ingredient nieuwIngredient = new Ingredient("testje", "testje2");
            modelAndView.addObject("nieuwIngredient", new Ingredient());
            List<Ingredient> ing = ingredientMgr.getAllIngredients();
            modelAndView.addObject("Ingredienten", ing);


        } else {
            modelAndView.setViewName("errorPagina");
        } */



    }

    @GetMapping(value = {"/ingredienten/verwijderen/{ingredientId}"})
    public ModelAndView DeleteIngredient(@PathVariable(required = true) Long ingredientId,Model model) {
        ingredientMgr.deleteIngredient(ingredientId);
        List<Ingredient> ingredienten = ingredientMgr.getAllIngredients();

        model.addAttribute("Ingredienten", ingredienten);
        model.addAttribute("nieuwIngredient", new Ingredient());








        return new ModelAndView("redirect:/ingredienten", (Map<String, ?>) model);
    }


}
