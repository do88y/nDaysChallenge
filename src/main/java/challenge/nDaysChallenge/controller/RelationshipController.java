package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/friends")

@RestController
public class RelationshipController {

    private final RelationshipRepository relationshipRepository;

    public Relationship makeFriends(@Valid @RequestBody RelationshipRepository relationshipRepository)throws Exception{
        return (Relationship) relationshipRepository;
    }
}
