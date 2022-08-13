package com.exament.apistack.restexp;

import com.exament.apistack.entity.User;
import com.exament.apistack.repository.UserRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Api(value = "User", description = "User CRUD operations")
public class RestExpController
{
    @Autowired
    UserRepository userRepository;

    @GetMapping
    @ApiOperation(value = "Find all users", notes = "Retrieving the collection of users", response = User[].class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = User[].class)
    })
    public ResponseEntity<Iterable<User>> findAll()
    {
        Iterable<User> users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user", notes = "Get an user by its id", response = User.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 404, message = "Not found", response = User.class)
    })
    public ResponseEntity<User> find(
            @ApiParam(required = true, name = "id", value = "ID of the user you want to delete")
            @PathVariable("id") Long id)
    {
        Optional<User> userData = userRepository.findById(id);
        return userData.map(
                user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiOperation(value = "Create user", notes = "Creating a new user", response = User.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 400, message = "Bad request", response = User.class)
    })
    public ResponseEntity<User> create(
            @ApiParam(required = true, name = "user", value = "New user")
            @RequestBody User user)
    {
        try {
            User _user_ = userRepository
                    .save(new User(
                            user.getId(),
                            user.getImage(), user.getName(),
                            user.getEmail(), user.getGender(),
                            user.getEstatus()));
            return new ResponseEntity<>(_user_, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update user", notes = "Updating an existing user", response = User.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 400, message = "Bad request", response = User.class),
            @ApiResponse(code = 404, message = "Not found", response = User.class)
    })
    public ResponseEntity<User> update(
            @ApiParam(required = true, name = "id", value = "ID of the user you want to update", defaultValue = "0")
            @PathVariable("id") Long id,
            @ApiParam(required = true, name = "user", value = "Updated user")
            @RequestBody User user)
    {
        Optional<User> userData = userRepository.findById(id);

        if(userData.isPresent()) {
            User _user_ = userData.get();
            _user_.setImage(_user_.getImage());
            _user_.setName(_user_.getName());
            _user_.setEmail(_user_.getEmail());
            _user_.setGender(_user_.getGender());
            _user_.setEstatus(_user_.getEstatus());
            return new ResponseEntity<>(userRepository.save(_user_), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user", notes = "Deleting an existing user")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Success"),
            @ApiResponse(code = 404, message = "Not found", response = User.class)
    })
    public ResponseEntity<HttpStatus> delete(
            @ApiParam(required = true, name = "id", value = "ID of the user you want to delete")
            @PathVariable("id") Long id)
    {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
