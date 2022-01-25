package com.kadal.restcontroller;

import com.kadal.entity.Category;
import com.kadal.redisentityrepo.RCategory;
import com.kadal.redisentityrepo.RCategoryRepository;
import com.kadal.repository.CategoryRepository;
import com.kadal.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;
import java.util.UUID;

import static com.kadal.response.ResponseEnum.*;

@RestController
@AllArgsConstructor
@RequestMapping("category")
public class CategoryRestController {
    final CategoryRepository categoryRepo;
    final RCategoryRepository rCategoryRepo;


    @PostMapping(path = "/add")
    public Response add(@RequestBody Category category) {

        Category c = categoryRepo.save(category);

        // Redis Save
        RCategory rCategories = new RCategory();
        rCategories.setCid(c.getId());
        rCategories.setTitle(c.getTitle());
        rCategories.setId(UUID.randomUUID().toString());
        rCategoryRepo.save(rCategories);

        return new Response(STATUS_TRUE.getResponse(), SUCCESSFUL_MESSAGE.getResponse(),
                c);
    }

    @GetMapping("list")
    public Response list(){
        return new Response(STATUS_TRUE.getResponse(),SUCCESSFUL_MESSAGE.getResponse()
                ,rCategoryRepo.findAll());
    }


    @DeleteMapping("delete/{id}")
    public Response delete(@PathVariable String id){
        Response response = new Response();
        try {
            int cid=Integer.parseInt(id);

            //db item delete
            categoryRepo.deleteById(cid);

            //redis item delete

            Optional<RCategory> optRc= rCategoryRepo.findByCid(cid);
            if(optRc.isPresent()){
                RCategory rc = optRc.get();
                rCategoryRepo.deleteById(rc.getId());

                response.setStatus(STATUS_TRUE.getResponse());
                response.setMessage(SUCCESSFUL_MESSAGE.getResponse());
                response.setResult(rc);
            }
            else {
                response.setStatus(STATUS_FALSE.getResponse());
                response.setMessage(ERROR_MESSAGE.getResponse());
            }

        }
        catch (Exception ex){
            response.setStatus(STATUS_FALSE.getResponse());
            response.setMessage(ERROR_MESSAGE.getResponse());
        }
        return response;

    }

    @PutMapping("/update")
    public Response update( @RequestBody Category category ) {
        Response response = new Response();

        // db update
        Optional<Category> optC = categoryRepo.findById(category.getId());
        if (optC.isPresent() ) {
            Category c = optC.get();
            c.setTitle(category.getTitle());
            categoryRepo.saveAndFlush(c);

            // Redis item update
            Optional<RCategory> optRc = rCategoryRepo.findByCid(category.getId());
            if ( optRc.isPresent() ) {
                RCategory rc = optRc.get();
                rc.setTitle(category.getTitle());

                // delete item
                rCategoryRepo.deleteById(rc.getId());
                rCategoryRepo.save(rc);

                response.setStatus(STATUS_TRUE.getResponse());
                response.setMessage(SUCCESSFUL_MESSAGE.getResponse());
                response.setResult(rc);

            }else {
                response.setStatus(STATUS_FALSE.getResponse());
                response.setMessage(ERROR_MESSAGE.getResponse()+ ": Redis Update");

            }

        }else {
            response.setStatus(STATUS_FALSE.getResponse());
            response.setMessage(ERROR_MESSAGE.getResponse()+ ": DB Update");
        }

        return response;
    }



}
