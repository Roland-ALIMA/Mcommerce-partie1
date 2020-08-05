package com.ecommerce.microcommerce.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exception.ProduitIntrouvableException;
import com.ecommerce.microcommerce.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Api(description = "Gestion des produits")
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    //produits
    @GetMapping(value = "products")
    public List<Product> listeProduits() {
        return productDao.findAll();
    }

    @PostMapping(value = "products")
    public ResponseEntity<Void> ajouterProduit(@RequestBody Product product) {
        Product product1 = productDao.save(product);
        if (product == null) {
            return ResponseEntity.noContent().build();
        }

        //uri du nouveau produit
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product1.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //products/{id}
    @ApiOperation(value = "Récupère un produit selon son ID")
    @GetMapping(value = "products/{id}")
    public Product afficherUnProduit(@PathVariable int id) throws ProduitIntrouvableException {

        Product product = productDao.findById(id);
        if (product == null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " n'existe pas");
        return product;
    }

    @GetMapping(value = "test/products/{prixLimit}")
    public List<Product> testDeRequetes(@PathVariable int prixLimit) {
        return productDao.findByPrixGreaterThan(prixLimit);
    }

    @DeleteMapping(value = "products/{id}")
    public void supprimerUnProduit(@PathVariable int id) {

        productDao.delete(id);
    }
}
