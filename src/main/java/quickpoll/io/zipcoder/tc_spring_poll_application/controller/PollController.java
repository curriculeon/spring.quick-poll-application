package quickpoll.io.zipcoder.tc_spring_poll_application.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import quickpoll.io.zipcoder.tc_spring_poll_application.model.Poll;
import quickpoll.io.zipcoder.tc_spring_poll_application.exception.ResourceNotFoundException;
import quickpoll.io.zipcoder.tc_spring_poll_application.repositories.PollRepository;
import quickpoll.io.zipcoder.tc_spring_poll_application.service.PollService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
public class PollController {
   @Inject
    private PollRepository pollRepository;

   @Inject
   private PollService service;



   @RequestMapping(value="/polls", method = RequestMethod.GET)
   public ResponseEntity<Iterable<Poll>> getAllPolls() {
       Iterable<Poll> allPolls = pollRepository.findAll();
       return new ResponseEntity<>(allPolls, HttpStatus.OK);
   }



   @RequestMapping(value = "/polls", method = RequestMethod.POST)
   public ResponseEntity<?> createPoll(@Valid @RequestBody Poll poll) {
       URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(poll.getId()).toUri();
       poll = pollRepository.save(poll);
       HttpHeaders header = new HttpHeaders();
       header.setLocation(newPollUri);
       return new ResponseEntity<>(header, HttpStatus.CREATED);
   }

   @RequestMapping(value = "/polls", params = {"page", "size"}, method = RequestMethod.GET)
   @ResponseBody
   public List<Poll> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
       Page<Poll> resultPage = service.findPaginated(page,size);
       if(page> resultPage.getTotalPages()){
           throw new ResourceNotFoundException();
       }


       return resultPage.getContent();
   }

   @RequestMapping(value="/polls/{pollId}", method = RequestMethod.GET)
    public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
       verifyPoll(pollId);
       Poll p = pollRepository.findOne(pollId);
       return new ResponseEntity<>(p, HttpStatus.OK);
   }


   @RequestMapping(value="/polls/{pollId}", method = RequestMethod.PUT)
   public ResponseEntity<?> updatePoll(@Valid @RequestBody Poll poll, @PathVariable Long pollId){
       verifyPoll(pollId);
       Poll p = pollRepository.save(poll);
       return new ResponseEntity<>(HttpStatus.OK);
   }

   @RequestMapping(value="/polls/{pollId}", method = RequestMethod.DELETE)
   public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
       verifyPoll(pollId);
       pollRepository.delete(pollId);
       return new ResponseEntity<>(HttpStatus.OK);
   }

   public void verifyPoll(Long pollId){
       if(pollRepository.findOne(pollId) == null){
           throw new ResourceNotFoundException("Poll Not Found");
       }
   }

}
