package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.adapter.entrypoint.api.model.SaveUserBodyDto
import com.example.service.user.adapter.entrypoint.api.model.UserDto
import com.example.service.user.application.port.entrypoint.api.ChangeUserEndpointPort
import com.example.service.user.application.port.entrypoint.api.FindUserEndpointPort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(private val changeUserEndpointPort: ChangeUserEndpointPort,
                     private val findUserEndpointPort: FindUserEndpointPort) {

    @RequestMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveUser(@RequestBody @Valid saveUserBodyDto: SaveUserBodyDto) =
            changeUserEndpointPort.saveUser(saveUserBodyDto)

    @GetMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    fun fetchUserById(@PathVariable("user_id") userId: Int) = findUserEndpointPort.fetchUserById(userId)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun fetchAllUsers(): Collection<UserDto> = findUserEndpointPort.fetchAllUsers()


    @PutMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@PathVariable("user_id") userId: Int,
                   @RequestBody saveUserBodyDto: @Valid SaveUserBodyDto) =
            changeUserEndpointPort.updateUser(userId, saveUserBodyDto)

    @DeleteMapping("/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserById(@PathVariable("user_id") userId: Int) = changeUserEndpointPort.deleteUser(userId)
}