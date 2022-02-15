package org.kramreiter.mtcg.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.kramreiter.mtcg.card.Card;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "trades")
public class Trade {
    String offeringUser;
    String cardOffer;
    String cardFilter;

    private static ObjectMapper mapper = new ObjectMapper();

    protected Trade() {}

    public Trade(User user, String offer, CardFilter req) {
        offeringUser = user.getUsername();
        cardOffer = offer;
        try {
            cardFilter = mapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Getter(AccessLevel.NONE)
    private Long id;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public boolean checkTrade(String offer) {
        try {
            CardFilter filter = mapper.readValue(cardFilter, CardFilter.class);
            if (filter.getCardId() != null) {
                if (filter.getCardId() == offer) {
                    
                }
            } else {

            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
