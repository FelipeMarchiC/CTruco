package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.otavio.lopes.teitasbot.TeitasBotFunctions;
import com.rossi.lopes.trucoguru.TrucoGuruUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.util.List;


import static com.bueno.spi.model.CardRank.THREE;
import static com.bueno.spi.model.CardSuit.HEARTS;
import static com.bueno.spi.model.CardSuit.SPADES;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TeitasBotFunctionsTest {
    private static GameIntel gameIntel;
    @BeforeEach
    public void setGameIntel() {
        gameIntel = mock(GameIntel.class);
    }

    @Nested
    @DisplayName("HasManilhaTest")
    class HasManilhaTest {
        @Test
        @DisplayName("Should return true if hand at least one manilha")
        void shouldReturnTrueIfHandHasManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TeitasBotFunctions.hasManilha(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if hand has no manilha")
        void shouldReturnFalseIfHandHasNoManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TeitasBotFunctions.hasManilha(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("Card that will be return")
    class CardThatWillBeReturnTest {
        @Test
        @DisplayName("Should return the second strong card for first round and nuts or strong hand")
        void shouldReturnSecondStrongCardIfFirstRoundAndStrongHand() {
            //the treatment of first round is at main
            TrucoCard vira  =  TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
            );
            assertEquals(TrucoCard.of(THREE, SPADES), TeitasBotFunctions.chooseCardToPlayAtFirstStrongHand(cards,vira).content());
        }
        @Test
        @DisplayName("Should return the strongest card if we won the first and strong hand")
        void shouldReturnStrongestCardIfFirstRound() {

            //the treatment of first round is at main
            TrucoCard vira  =  TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            assertEquals(TrucoCard.of(THREE, SPADES), TeitasBotFunctions.chooseCardToPlaySecondIfWonFirst(cards,vira).content());
        }
    }

    @Nested
    @DisplayName("TypesOfHand")
    class TypesOfHands{
        @Test
        @DisplayName("Should return true if we had a nuts hand")
        void shouldReturnTrueIfHandIsNuts() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );
            assertThat(TeitasBotFunctions.hasNutsHand(cards,vira)).isTrue();

        }

        @Test
        @DisplayName("Should return true if we had a good hand")
        void shouldReturnTrueIfHandIsGood() {

            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );
            assertThat(TeitasBotFunctions.hasGoodHand(cards,vira)).isTrue();

        }
    }

    @Test
    @DisplayName("Should return true if we had a trash hand")
    void shouldReturnTrueIfHandIsTrash() {
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
        );
        assertThat(TeitasBotFunctions.hasTrashHand(cards,vira)).isTrue();

    }

    @Test
    @DisplayName("Should return True if we ha Strong Hand --  lowest than nuts and better than trash")
    void shouldReturnTrueIfHandIsStrong() {
        TrucoCard vira =  TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );
        assertThat(TeitasBotFunctions.hasStrongHand(cards,vira)).isFalse();
    }


    @Nested
    class WeStart{
        @Test
        @DisplayName("should return true if we last round")
        void shouldReturnTrueIfWeStart(){
            assertThat(TeitasBotFunctions.firstToPlay(gameIntel)).isTrue()    ;
        }
    }

    @Nested
    class TypesOfCards{
        @Test
        @DisplayName("Should return the weakest card of the hand")
        void shouldReturnStrongestCard(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.SIX, TeitasBotFunctions.getStrongestCard(cards, vira).getRank());
            assertEquals(CardSuit.DIAMONDS, TeitasBotFunctions.getStrongestCard(cards, vira).getSuit());
        }

        //assert is different, we need to compare the card.

        @Test
        @DisplayName("Should return the weakest card of the hand")
        void shouldReturnWeakestCard(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.FOUR, TeitasBotFunctions.getWeakestCard(cards, vira).getRank());
            assertEquals(CardSuit.DIAMONDS, TeitasBotFunctions.getWeakestCard(cards, vira).getSuit());
        }

        @Test
        @DisplayName("Should return the miiddle level card of the hand")

        void shouldReturnMiddleLevelCard(){
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.TWO, TeitasBotFunctions.getMiddleCardLevel(cards, vira).getRank());
            assertEquals(CardSuit.SPADES, TeitasBotFunctions.getMiddleCardLevel(cards, vira).getSuit());

        }
    }

    @Nested
    class PlayModes{
        @Test
        @DisplayName("Should return true if !goodhand ")
        //if we ha a good hand, press
        //good hand = manilha and 3
        void ShouldReturnTrueIFGoodHand(){
            TrucoCard vira = TrucoCard.of(CardRank.KING,CardSuit.CLUBS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
            
            assertThat(TeitasBotFunctions.PlayAgressiveMode(cards,vira, gameIntel)).isTrue();
        }

        @Test
        @DisplayName("Should play at safe mode, if we had and strong to nuts hand")
        void ShouldReturnTrueIfNutsOrStrongHand(){

            TrucoCard vira = TrucoCard.of(CardRank.ACE,CardSuit.CLUBS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );
            assertThat(TeitasBotFunctions.PlaySafeMood(cards,vira, gameIntel)).isTrue();
        }
    }





}