package kdg.be.Controllers;

import kdg.be.Managers.IIngredientHoeveelheidManager;
import kdg.be.Managers.IIngredientManager;
import kdg.be.Managers.IProductManager;
import kdg.be.Managers.IngredientHoeveelheidManager;
import kdg.be.Models.IngHoeveelheidPaar;
import kdg.be.Models.Ingredient;
import kdg.be.Models.Product;
import kdg.be.Models.ProductState;
import kdg.be.RabbitMQ.RabbitSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class ProductController {
    //Modelbinding vb
    //Controller advice vb
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final IIngredientManager ingredientManager;
    private final IProductManager productManager;
    private final RabbitSender rabbitSender;

    private  final IIngredientHoeveelheidManager ingredientHoeveelheidManager;
    public ProductController(IIngredientManager ingredientManager, IProductManager productManager, IngredientHoeveelheidManager ingredientHoeveelheidManager, RabbitSender rabbitSender) {

        this.ingredientManager = ingredientManager;
        this.productManager = productManager;
        this.rabbitSender = rabbitSender;
        this.ingredientHoeveelheidManager=ingredientHoeveelheidManager;
    }

    @GetMapping("/")
    public String ToonHomePage(){



        return "redirect:/producten";
    }


    @GetMapping(value={"/producten"})
    public String AllProducts(Model model) {

        logger.info("test");
        List<Product> alleProducten = productManager.getAllProducts();
        System.out.println(alleProducten.size());
        model.addAttribute("Producten", alleProducten);
     return "Producten/ProductenOverzicht";
    }

    @GetMapping(value={"/producten/create","/producten/create/{productId}"})
    public String Newproduct(Model model, @PathVariable(required = false) Integer productId) {
Product product=null;
        List<Ingredient> alleIngredienten = ingredientManager.getAllIngredients();

        if(productId==null) {
            product = new Product();
            product.getSteps().add("begin");

           /*    product.getComposition().add(alleIngredienten.get(0));
            product.getAmounts().add(5.0);
         product.getComposition().add(new Ingredient("", ""));
            product.getAmounts().add(0.0);*/
        }
        else{

            Optional<Product> optionalProduct=  productManager.getProductById(productId);
            if(optionalProduct.isPresent()){

                product=optionalProduct.get();
            product.setProductId(Long.valueOf(productId));

            }
            else{
                product=new Product("",new ArrayList<String>(),new ArrayList<>(),new ArrayList<>());
            }

        }

        model.addAttribute("product", product);


        model.addAttribute("alleIngredienten", alleIngredienten);

        return "Producten/newProduct";
    }



    @PostMapping(value="/producten/create")
    public ModelAndView saveProduct(Model model, Product product) {
        System.out.println("product/create");
if(product.getComposition().size()!=product.getComposition().stream().distinct().toList().size()){
    product.getComposition().remove(product.getComposition().size()-1);
    product.getAmounts().remove(product.getAmounts().size()-1);


}

        productManager.saveOrUpdate(product);
      List<Ingredient>alleIngredienten=  ingredientManager.getAllIngredients();

        model.addAttribute("alleIngredienten",alleIngredienten);
System.out.println("saveProduct");
        System.out.println(product.getComposition().size());

        return new ModelAndView("redirect:/producten", (Map<String, ?>) model);
    }
    @PostMapping(value="/producten/create", params="stap")
    public ModelAndView AddStep(Model model, Product product) {
product.getSteps().add("voorbeeld");
        model.addAttribute("product", product);

        return new ModelAndView("Producten/newProduct", (Map<String, ?>) model);
    }
    @PostMapping(value="/producten/create", params="addIng")
    public ModelAndView AddIng(Model model, Product product) {
System.out.println("?");

        List<Ingredient> alleIngredienten = ingredientManager.getAllIngredients();

        if(product.getComposition().stream().distinct().toList().size()!=product.getComposition().size()){




            model.addAttribute("error","this ingredient is already part of the recepy");




        } else if
        (alleIngredienten.size()>0){
            product.getComposition().add(alleIngredienten.get(0));
            product.getAmounts().add(0.0);



        }




        model.addAttribute("product", product);
        model.addAttribute("alleIngredienten", alleIngredienten);

        return new ModelAndView("Producten/newProduct", (Map<String, ?>) model);


    }
    @PostMapping(value="/producten/create", params ="removeIng" )
    public ModelAndView RemoveIng(Model model, Product product, @RequestParam(name = "removeIng") int removeIng) {
     System.out.println("removeIng is "+removeIng);
        System.out.println(product.getComposition().size());
        product.getAmounts().remove(removeIng);

        product.getComposition().remove(removeIng);
        System.out.println(product.getComposition().size());

        model.addAttribute("product", product);
        List<Ingredient> alleIngredienten=ingredientManager.getAllIngredients();
        model.addAttribute("alleIngredienten",alleIngredienten);
        return new ModelAndView("Producten/newProduct", (Map<String, ?>) model);
    }
@PostMapping(value = "/test")
    public String Tijdelijk(IngHoeveelheidPaar ingHoeveelheidPaar, Product product){
        System.out.println(ingHoeveelheidPaar.getAantal());
System.out.println(ingHoeveelheidPaar.getIngredient().getName());
  return "sample";

}


@GetMapping("/RT")
public String RestRouteRabbit(){
Product product=new Product();
product.setName("test");
product.getSteps().add("stap 1")      ;
product.getSteps().add("stap 2")      ;


    rabbitSender.sendNewRecipe(product);


        return "sample";
}


@GetMapping("/producten/final/{productId}")
public String MakeFinal(@PathVariable int productId){

Optional<Product> optioneelProduct=productManager.getProductById(productId);

if(optioneelProduct.isPresent()){
    Product product =optioneelProduct.get();
    product.set_ProductStatus(ProductState.Finaal);
    System.out.println("bij het finaal maken");
    System.out.println(product.getComposition().size());

    rabbitSender.sendNewRecipe(product);
productManager.saveOrUpdate(product);

}

    return "redirect:/producten";

}

    @GetMapping("/producten/deactivate/{productId}")
    public String Deactivate(@PathVariable int productId){

        Optional<Product> optioneelProduct=productManager.getProductById(productId);

        if(optioneelProduct.isPresent()){
            Product product =optioneelProduct.get();
            product.set_ProductStatus(ProductState.Gedeactiveerd);
            System.out.println("bij het deactiveren maken");
            System.out.println(product.getComposition().size());
            rabbitSender.sendNewRecipe(product);
            productManager.saveOrUpdate(product);

        }

        return "redirect:/producten";




    }


@GetMapping("/producten/product/detail/{productId}")
    public ModelAndView DetailPage(@PathVariable Long productId,Model model){


     Optional<Product>optionalProduct=   productManager.getProductById(productId);
        if(optionalProduct.isPresent()){
            model.addAttribute("product",optionalProduct.get());

            return new ModelAndView("Producten/productDetail", (Map<String, ?>) model);
        }


        else return new ModelAndView("redirect:/Producten/ProductenOverzicht");





        }


}





  /*  @GetMapping("/producten/product/detail/{productId}")
    public String ProductDetailpage(Model model, @PathVariable Long productId) {

        Optional<Product> mogelijkProduct = productManager.getProductById(productId);
        if (mogelijkProduct.isPresent()) {
            Product product = mogelijkProduct.get();
            model.addAttribute("Product", product);


            return "Producten/ProductDetailPagina";
        } else {
            //voorlopig
            return "errorPagina";
        }
    }


    @GetMapping(value = {"/producten/product/bewerk", "/producten/product/bewerk/{productId}"})
    public String ProductEditPage(Model model, @PathVariable(required = false) Long productId) {
        System.out.println("gesaved");

        if (productId != null) {
            Optional<Product> mogelijkProduct = productManager.getProductById(productId);
            if (mogelijkProduct.isPresent()) {
                Product product = mogelijkProduct.get();
                List<Ingredient> ingredienten = ingredientManager.getAllIngredients();
                model.addAttribute("Product", product);
                model.addAttribute("ingredienten", ingredienten);


                return "Producten/ProductBewerken";
            } else {
                System.out.println(productId);
                //voorlopig
                return "errorPagina";
            }

        } else {
            Product product = new Product();
            model.addAttribute("Product", product);
            return "Producten/NieuwProduct";

        }

    }

    @PostMapping(value = {"producten/product/bewerk", "/producten/product/bewerk/{productId}"}, params = "finaliseer")
    public ModelAndView FinaliseProduct(Product binnekomendProduct) {
        System.out.println("Finaliseer");
        Optional<Product> optioneelProduct = productManager.getProductById(binnekomendProduct.getProductId());
        if (optioneelProduct.isPresent()) {
            Product product = optioneelProduct.get();
            product.setProductStatus(ProductState.Finaal);
            productManager.saveProduct(product);

            ModelAndView modelAndView = new ModelAndView("/Producten/ProductenOverzicht");
            modelAndView.addObject("Producten", productManager.getAllProducts());
            modelAndView.addObject("Product", new Product());
            return modelAndView;

        } else {
            return new ModelAndView("errorPagina");

        }
    }


    @GetMapping(value = {"producten/product/bewerk", "/producten/product/bewerk/{productId}"}, params = "deactiveer")
    public ModelAndView DeactivateProduct(@PathVariable Long productId) {
        System.out.println("deactiveer");
        Optional<Product> optioneelProduct = productManager.getProductById(productId);
        if (optioneelProduct.isPresent()) {
            Product product = optioneelProduct.get();

            product.setProductStatus(ProductState.Gedeactiveerd);
            productManager.saveProduct(product);
            ModelAndView modelAndView = new ModelAndView("/Producten/ProductenOverzicht");
            modelAndView.addObject("Producten", productManager.getAllProducts());
            modelAndView.addObject("Product", new Product());
            return modelAndView;

        } else {
            return new ModelAndView("errorPagina");
        }

    }

    @PostMapping(value = {"producten/product/bewerk", "/producten/product/bewerk/{productId}"})
    public ModelAndView SaveProduct(Product product) {

        System.out.println(product.getName());
        productManager.saveProduct(product);
        List<Product> producten = productManager.getAllProducts();

        ModelAndView modelAndView = new ModelAndView("Producten/ProductenOverzicht");
        modelAndView.addObject("Producten", producten);
        modelAndView.addObject("Product", product);
        return modelAndView;
    }


    @PostMapping(value = {"producten/product/bewerk", "/producten/product/bewerk/{productId}"}, params = "stap=toevoegen")
    public ModelAndView AddStep(Product product) {

        product.getSteps().add("");
        ModelAndView modelAndView = new ModelAndView("Producten/ProductBewerken");
        modelAndView.addObject("Product", product);
        return modelAndView;
    }


    @GetMapping("/test")
    public String test() {
        HashMap<Ingredient, Double> composition = new HashMap<>();
        Ingredient ingredientOne = this.ingredientManager.saveIngredient(new Ingredient("testIgredient", "testBeschrijving"));
        Ingredient ingredientTwo = this.ingredientManager.saveIngredient(new Ingredient("testIgredient2", "testBeschrijving"));
        composition.put(ingredientOne, 50d);
        composition.put(ingredientTwo, 50d);
        Product product = this.productManager.saveProduct(new Product("testRecept", List.of("testBeschrijving"), composition)
        );

        this.rabbitSender.sendNewRecipe(product);
        return "errorPagina";
    }
*/


