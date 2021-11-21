package org.messager.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.messager.exceptions.BadRequestException;
import org.messager.exceptions.MessageException;
import org.messager.exceptions.MessageNotExists;
import org.messager.dao.MessageDAO;
import org.messager.dao.ResponseMessage;
import org.messager.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@Tag(name = "Message resource API", description = "API allows you to manipulate data Message resources")
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    private final IMessageService service;


    @Autowired
    public MessageController(IMessageService service) {
        this.service = service;
    }

    @Operation(summary = "Get All Message by page")
    @GetMapping(path = "/all", produces = "application/json")
    public List<MessageDAO> getAllMessagesByPage(
            @Parameter(description = "Set page of data") @RequestParam("page") Integer page,
            @Parameter(description = "Set size of selected data") @RequestParam("size") Integer size,
            @Parameter(description = "Set sortDirection. Parameter is optional. Using with sortField") @RequestParam("sortDirection") Optional<String> sortDirection,
            @Parameter(description = "Set field for sortDirection. Parameter is optional. Using with sortDirection") @RequestParam("sortField") Optional<String> sortField) {

        return service.getListOfMessagesByPage(page, size, sortDirection, sortField);
    }


    @Operation(summary = "Get Message by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Message",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDAO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public MessageDAO getMessageWithID(@Parameter(description = "Get Message with Id") @PathVariable("id") Long id) {
        log.info("Get Message with ID: {}", id);
        return service.getMessageById(id);
    }

    @Operation(summary = "Create new Message")
    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseMessage> createMessage(@Parameter(description = "Message body") @RequestBody MessageDAO messageDAO) {
        log.info("Create message. MessageBody is {}", messageDAO);
        if(messageDAO.getId() != null) {
            log.warn("PostRequest. Create message. Error field id is not null");
            throw new BadRequestException("Field must be null or empty");
        }
        log.info("PostRequest. Create message. Saving to database");
        service.createNewMessage(messageDAO);
        return new ResponseEntity<>(new ResponseMessage(0, "Success created"), HttpStatus.OK);
    }

    @Operation(summary = "Update data of message and save into DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
    })
    @PutMapping(path = "/update", produces = "application/json")
    public ResponseEntity<ResponseMessage> updateMessage(@Parameter(description = "Message body") @RequestBody MessageDAO messageDAO) {
        log.info("Put Request. Updated message {}", messageDAO.toBuilder());
        if(messageDAO.getId() == null) {
            log.warn("PostRequest. Create message. Error field id is null");
            throw new BadRequestException("Field must be not null or empty");
        }
        log.info("Put Request. Update message. Saving to database ...");

        try {
            service.setMessage(messageDAO);
        } catch (MessageNotExists e) {
            log.warn("Message not found with id {}", messageDAO.getId(), e.getMessage());
            throw new MessageException("Error with request", e);
        }

        return new ResponseEntity<>(new ResponseMessage(0, "Success updated"), HttpStatus.OK);
    }


    @Operation(summary = "Delete Message by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping(path= "/delete/{id}", produces = "application/json")
    public ResponseEntity<ResponseMessage> deleteMessage(@PathVariable("id") Long id)  {
        log.info("Delete Request. Delete message by ID: {}", id);
        try {
            service.deleteMessage(id);
        } catch (MessageNotExists e) {
            throw new MessageException("Exception operation with message", e);
        }
        return new ResponseEntity<>(new ResponseMessage(0, "Success deleted"), HttpStatus.OK);

    }

    @ExceptionHandler
    public ResponseEntity<ResponseMessage> notFoundException(MessageException e) {
        return new ResponseEntity<>(new ResponseMessage(404, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMessage> badRequestException(BadRequestException e) {
        return new ResponseEntity<>(new ResponseMessage(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }





}
