package com.exament.apistack.controller;

import com.exament.apistack.entity.User;
import com.exament.apistack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class UserController
{
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @GetMapping("create")
    public String showAddUser(User user)
    {
        return "add-user";
    }

    @GetMapping
    public String showIndexForm(
            // @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable),
            Model model)
    {
        //model.addAttribute("users", userRepository.findAll(pageable));
        //return "index";
        return findPaginated(1, "name", "asc", model);
    }

    @GetMapping("page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model)
    {
        int pageSize = 10;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<User> page = userRepository.findAll(pageable);
        List<User> listUsers = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("users", listUsers);

        return "index";
    }

    /*
    @PostMapping("add")
    public String addUser(User user, BindingResult result, Model model)
    {
        if(result.hasErrors()) {
            return "add-user";
        }

        userRepository.save(user);
        return "redirect:/";
    }
    */

    /*
    @PostMapping("/upload") public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        return "imageupload/index";
    }
    */

    @PostMapping("add")
    public String addUser(Model model, @RequestParam("image_file") MultipartFile file, User user, BindingResult result)
            throws IOException
    {
        if(result.hasErrors()) {
            return "add-user";
        }

        User __user = new User();
        // String image = file.getOriginalFilename();
        __user.setContent(file.getBytes());

        __user.setName(user.getName());
        __user.setEmail(user.getEmail());
        __user.setGender(user.getGender());
        __user.setEstatus(user.getEstatus());
        userRepository.save(__user);

        // Asigna la imagen después de guardar para obtener
        //  el ID del usuario.
        __user.setImage("http://localhost:8080/image?id=" + __user.getId());
        userRepository.save(__user);

        return "redirect:/";
    }

    @GetMapping("image")
    public void showImage(@Param("id") Long id, HttpServletResponse response, Optional<User> user)
            throws IOException, ServletException {
        user = userRepository.findById(id);
        response.setContentType("image/jpeg, image/jpg, image/png");
        response.getOutputStream().write(user.get().getContent());
        response.getOutputStream().close();
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model)
    {
        Optional<User> userData = userRepository.findById(id);
        if(userData.isPresent()) {
            User __user = userData.get();
            __user.setImage(__user.getImage());
            __user.setContent(__user.getContent());
            __user.setName(__user.getName());
            __user.setEmail(__user.getEmail());
            __user.setGender(__user.getGender());
            __user.setEstatus(__user.getEstatus());
            model.addAttribute("user", __user);
        }

        return "update-user";
    }

    @PostMapping("update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user, BindingResult result, Model model,
                             @RequestParam("image_file") MultipartFile file) throws IOException {
        if(result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        if(!file.isEmpty()) {
            user.setImage("http://localhost:8080/image?id=" + user.getId());
            user.setContent(file.getBytes());
        } else {
            Optional<User> userData = userRepository.findById(id);
            if(userData.isPresent()) {
                User __user = userData.get();
                user.setImage("http://localhost:8080/image?id=" + user.getId());
                user.setContent(__user.getContent());
            }
        }

        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model)
    {
        userRepository.deleteById(id);
        model.addAttribute("users", userRepository.findAll());

        return "redirect:/";
    }
}
