package quickpoll.io.zipcoder.tc_spring_poll_application.controller;

import quickpoll.io.zipcoder.tc_spring_poll_application.model.Vote;
import quickpoll.io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;

@RestController
public class VoteController {
    @Inject
    private VoteRepository voteRepository;

    @RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.POST)
    public ResponseEntity<?> createVote(@PathVariable Long pollId, @RequestBody Vote vote) {
        vote = voteRepository.save(vote);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(vote.getId()).toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/polls/votes", method = RequestMethod.GET)
    public Iterable<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    @RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.GET)
    public Iterable<Vote> getVote(@PathVariable Long pollId) {
        return voteRepository.findVotesByPoll(pollId);
    }
}
