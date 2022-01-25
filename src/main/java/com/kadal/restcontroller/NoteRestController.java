package com.kadal.restcontroller;

import com.kadal.entity.Note;
import com.kadal.repository.NoteRepository;
import com.kadal.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.kadal.response.ResponseEnum.*;


@RestController
@AllArgsConstructor
@RequestMapping("note")
public class NoteRestController {

    final NoteRepository noteRepo;

    @PostMapping("add")
    public Response add(@RequestBody Note note) {
        return new Response(STATUS_TRUE.getResponse(),SUCCESSFUL_MESSAGE.getResponse(),noteRepo.save(note));
    }

    @GetMapping("list")
    public Response list() {
        return new Response(STATUS_TRUE.getResponse(),SUCCESSFUL_MESSAGE.getResponse(),noteRepo.findAll());
    }

    @DeleteMapping("delete/{id}")
    public Response delete(@PathVariable(name = "id") String id){
        Response response = new Response();
        Integer nid = Integer.parseInt(id);

        Optional<Note> optNote = noteRepo.findById(nid);
        if(optNote.isPresent()){
            noteRepo.deleteById(nid);

            response.setStatus(STATUS_TRUE.getResponse());
            response.setMessage(SUCCESSFUL_MESSAGE.getResponse());
            response.setResult(optNote);
        }
        else{
            response.setStatus(STATUS_FALSE.getResponse());
            response.setMessage(ERROR_MESSAGE.getResponse());
        }
        return response;
    }

    @PutMapping("/update")
    public Response update(@RequestBody Note note){

        Response response = new Response();

        Optional<Note> optNote= noteRepo.findById(note.getId());
        if(optNote.isPresent()){
            Note n =  optNote.get();
            n.setTitle(note.getTitle());
            n.setDetail(note.getDetail());
            noteRepo.saveAndFlush(n);

            response.setStatus(STATUS_TRUE.getResponse());
            response.setMessage(SUCCESSFUL_MESSAGE.getResponse());
            response.setResult(n);
        }
        else{
            response.setStatus(STATUS_FALSE.getResponse());
            response.setMessage(ERROR_MESSAGE.getResponse());
        }

        return response;
    }

}
