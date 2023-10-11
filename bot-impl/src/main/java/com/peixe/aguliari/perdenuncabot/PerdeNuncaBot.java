/*
 *  Copyright (C) 2023 Eduardo Aguliari and Ramon Peixe
 *  Contact: eduardo <dot> aguliari <at> ifsp <dot> edu <dot> br
 *  Contact: ramon <dot> peixe <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */
package com.peixe.aguliari.perdenuncabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class PerdeNuncaBot implements BotServiceProvider {
    private static final List<CardRank> offCards = List.of(CardRank.ACE, CardRank.TWO, CardRank.THREE);

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        for (TrucoCard card : intel.getCards()) {
            if (card.isOuros(intel.getVira())) {
                return CardToPlay.of(card);
            }
        }

        if (intel.getRoundResults().isEmpty()) {
            TrucoCard smallestAttackCard = getLowestAttackCard(intel);
            if (smallestAttackCard != null) {
                return CardToPlay.of(smallestAttackCard);
            }
        }

        // Choose the card with the lowest relative value to the opponent's card.
        TrucoCard smallestCardThatCanWin = chooseSmallestCardThatCanWin(intel);

        // If there is no card that can win, choose the card with the lowest relative value to the vira card.
        if (smallestCardThatCanWin == null) {
            smallestCardThatCanWin = getSmallestCardInHand(intel);
        }

        // Return the chosen card.
        return CardToPlay.of(smallestCardThatCanWin);
    }

    // This method gets the lowest attack card in the player's hand.
    private static TrucoCard getLowestAttackCard(GameIntel intel) {
        // Get a list of all attack cards in the player's hand.
        List<TrucoCard> attackCards = intel.getCards().stream()
                .filter(card -> offCards.contains(card.getRank()))
                .toList();

        // If the player has at least two attack cards, return the lowest attack card.
        if (attackCards.size() >= 2) {
            return attackCards.stream()
                    .min(TrucoCard::relativeValue)
                    .get();
        }

        // If the player has no attack cards, return null.
        return null;
    }

    private TrucoCard chooseSmallestCardThatCanWin(GameIntel intel) {
        // Get the opponent's card, if any.
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        // If there is no opponent's card, return null.
        if (opponentCard.isEmpty()) {
            return null;
        }

        // Get the vira card.
        TrucoCard vira = intel.getVira();

        // Initialize the card with the lowest relative value to the vira card.
        TrucoCard smallestCardThatCanWin = null;

        // Iterate over all the cards in the player's hand.
        for (TrucoCard card : intel.getCards()) {
            // If the current card is greater than the opponent's card, update the card with the lowest relative value to the vira card.
            if (card.relativeValue(vira) > opponentCard.get().relativeValue(vira)) {
                smallestCardThatCanWin = card;
            }
        }

        // Return the card with the lowest relative value to the vira card.
        return smallestCardThatCanWin;
    }

    private TrucoCard getSmallestCardInHand(GameIntel intel) {
        // Initialize the card with the lowest relative value to the vira card.
        TrucoCard smallestCard = null;

        // Get the vira card.
        TrucoCard vira = intel.getVira();

        // Iterate over all the cards in the player's hand.
        for (TrucoCard card : intel.getCards()) {
            // If the current card is less than the card with the lowest relative value to the vira card, update the card with the lowest relative value to the vira card.
            if (smallestCard == null || card.relativeValue(vira) < smallestCard.relativeValue(vira)) {
                smallestCard = card;
            }
        }

        // Return the card with the lowest relative value to the vira card.
        return smallestCard;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
